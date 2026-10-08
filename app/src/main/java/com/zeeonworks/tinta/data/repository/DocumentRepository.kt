package com.zeeonworks.tinta.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import com.zeeonworks.tinta.data.model.DocumentArchive
import com.zeeonworks.tinta.data.preferences.ArchivePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets
import java.util.UUID

data class DocumentResult(
    val archive: DocumentArchive,
    val content: String
)

class BinaryContentException(message: String = "Unable to decode textual contents of this dispatch.") : Exception(message)

class DocumentRepository(
    private val context: Context,
    private val preferences: ArchivePreferences = ArchivePreferences(context)
) {

    val archivesFlow: Flow<List<DocumentArchive>> = preferences.archivesFlow

    fun takePersistableUriPermission(uri: Uri) {
        try {
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flags)
        } catch (e: SecurityException) {
            // Some providers (e.g. FileProvider, WhatsApp, downloads) do not support persistable permissions
        } catch (e: Exception) {
            // Ignore other unexpected provider errors
        }
    }

    suspend fun openAndCacheDocument(uri: Uri): Result<DocumentResult> = withContext(Dispatchers.IO) {
        runCatching {
            takePersistableUriPermission(uri)

            val existing = preferences.getArchive(uri.toString())
            val archiveId = if (!existing?.id.isNullOrBlank()) {
                existing.id
            } else {
                UUID.randomUUID().toString().replace("-", "").take(12)
            }

            var content: String? = null

            // Attempt 1: Read directly from incoming Uri
            try {
                content = readTextFromUri(uri)
            } catch (ex: Exception) {
                // If reading directly from Uri fails (e.g. expired permission), check if we already have a cached local copy
                if (existing != null && existing.localFilePath.isNotBlank()) {
                    val cachedFile = File(existing.localFilePath)
                    if (cachedFile.exists()) {
                        content = cachedFile.readText(StandardCharsets.UTF_8)
                    }
                }
                if (content == null) {
                    throw ex
                }
            }

            val fileName = queryDisplayName(uri)
            val title = cleanTitle(fileName)
            val wordCount = calculateWordCount(content)
            val snippet = generateSnippet(content)

            // Cache content locally in app internal files directory
            val dispatchesDir = File(context.filesDir, "dispatches").apply { mkdirs() }
            val localFile = File(dispatchesDir, "dispatch_$archiveId.md")
            localFile.writeText(content, StandardCharsets.UTF_8)

            val archive = DocumentArchive(
                id = archiveId,
                uriString = uri.toString(),
                title = title,
                lastOpenedEpoch = System.currentTimeMillis(),
                wordCount = wordCount,
                snippet = snippet,
                localFilePath = localFile.absolutePath
            )

            preferences.saveOrUpdateArchive(archive)

            DocumentResult(archive = archive, content = content)
        }
    }

    suspend fun loadDocumentByIdOrUri(identifier: String): Result<DocumentResult> = withContext(Dispatchers.IO) {
        runCatching {
            val archive = preferences.getArchive(identifier)

            if (archive != null) {
                var content: String? = null

                // First priority: Read from permanent local sandbox copy
                if (archive.localFilePath.isNotBlank()) {
                    val localFile = File(archive.localFilePath)
                    if (localFile.exists()) {
                        content = localFile.readText(StandardCharsets.UTF_8)
                    }
                }

                // If local file wasn't found or as a refresh attempt, try reading from original Uri if available
                if (archive.uriString.isNotBlank()) {
                    try {
                        val uri = Uri.parse(archive.uriString)
                        val freshContent = readTextFromUri(uri)
                        content = freshContent

                        // Update local file cache
                        val dispatchesDir = File(context.filesDir, "dispatches").apply { mkdirs() }
                        val localFile = File(dispatchesDir, "dispatch_${archive.id}.md")
                        localFile.writeText(freshContent, StandardCharsets.UTF_8)
                    } catch (e: Exception) {
                        // If reading from original Uri fails (e.g. WhatsApp revoked temporary permission),
                        // continue using our local copy if available!
                    }
                }

                if (content != null) {
                    val updated = archive.copy(lastOpenedEpoch = System.currentTimeMillis())
                    preferences.saveOrUpdateArchive(updated)
                    return@runCatching DocumentResult(archive = updated, content = content)
                }

                throw FileNotFoundException("The dispatch source file could not be found or access has expired.")
            } else {
                // Not in preferences yet: Try opening as a Uri
                val uri = Uri.parse(identifier)
                openAndCacheDocument(uri).getOrThrow()
            }
        }
    }

    suspend fun removeArchive(archive: DocumentArchive) = withContext(Dispatchers.IO) {
        if (archive.localFilePath.isNotBlank()) {
            try {
                val file = File(archive.localFilePath)
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: Exception) {
                // Ignore deletion errors
            }
        }
        preferences.removeArchive(archive.id)
    }

    suspend fun removeArchiveById(id: String) = withContext(Dispatchers.IO) {
        val archive = preferences.getArchive(id)
        if (archive != null) {
            removeArchive(archive)
        } else {
            preferences.removeArchive(id)
        }
    }

    private fun readTextFromUri(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw FileNotFoundException("Could not open input stream for dispatch URI: $uri")

        inputStream.use { stream ->
            val bytes = stream.readBytes()
            if (isBinaryData(bytes)) {
                throw BinaryContentException()
            }
            return String(bytes, StandardCharsets.UTF_8)
        }
    }

    private fun isBinaryData(bytes: ByteArray): Boolean {
        val sampleSize = minOf(bytes.size, 4096)
        var nonPrintableCount = 0

        for (i in 0 until sampleSize) {
            val byteVal = bytes[i].toInt() and 0xFF
            if (byteVal == 0) {
                return true
            }
            if (byteVal < 32 && byteVal != 9 && byteVal != 10 && byteVal != 13) {
                nonPrintableCount++
            }
        }

        return sampleSize > 0 && (nonPrintableCount.toDouble() / sampleSize) > 0.05
    }

    private fun queryDisplayName(uri: Uri): String {
        var name: String? = null
        try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        name = it.getString(nameIndex)
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore query exceptions
        }

        if (name.isNullOrBlank()) {
            name = uri.lastPathSegment?.substringAfterLast('/')
        }

        return if (name.isNullOrBlank()) "Untitled Dispatch" else name!!
    }

    private fun cleanTitle(filename: String): String {
        return filename
            .replace(Regex("(?i)\\.(md|markdown|txt)$"), "")
            .replace('_', ' ')
            .trim()
            .ifBlank { "Untitled Dispatch" }
    }

    private fun calculateWordCount(text: String): Int {
        if (text.isBlank()) return 0
        return text.trim().split(Regex("\\s+")).filter { it.isNotBlank() }.size
    }

    private fun generateSnippet(text: String): String {
        val lines = text.lineSequence()
            .map { it.trim() }
            .filter { line ->
                line.isNotBlank() &&
                        !line.startsWith("#") &&
                        !line.startsWith("```") &&
                        !line.startsWith("---") &&
                        !line.startsWith("===") &&
                        !line.startsWith(">")
            }
            .take(3)
            .toList()

        val joined = lines.joinToString(" ")
            .replace(Regex("[*`_~#\\[\\]()]"), "")
            .trim()

        return if (joined.length > 150) {
            joined.take(147) + "..."
        } else if (joined.isEmpty()) {
            "No preliminary text found in this dispatch."
        } else {
            joined
        }
    }
}
