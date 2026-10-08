package com.zeeonworks.tinta

import com.zeeonworks.tinta.data.model.DocumentArchive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.commonmark.node.Heading
import org.commonmark.node.Paragraph
import org.commonmark.parser.Parser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentArchiveTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Test
    fun documentArchive_serializationAndDeserialization_isAccurate() {
        val original = DocumentArchive(
            id = "arch_123",
            uriString = "content://com.android.providers.media.documents/document/123",
            title = "The Vintage Gazette Editorial",
            lastOpenedEpoch = 1791234567890L,
            wordCount = 1420,
            snippet = "In the heart of the printing press era, ink met parchment...",
            localFilePath = "/data/data/com.zeeonworks.tinta/files/dispatches/dispatch_arch_123.md"
        )

        val serialized = json.encodeToString(listOf(original))
        val deserialized = json.decodeFromString<List<DocumentArchive>>(serialized)

        assertEquals(1, deserialized.size)
        val item = deserialized[0]
        assertEquals(original.id, item.id)
        assertEquals(original.uriString, item.uriString)
        assertEquals(original.title, item.title)
        assertEquals(original.lastOpenedEpoch, item.lastOpenedEpoch)
        assertEquals(original.wordCount, item.wordCount)
        assertEquals(original.snippet, item.snippet)
        assertEquals(original.localFilePath, item.localFilePath)
    }

    @Test
    fun titleSanitization_removesFileExtensionsProperly() {
        fun cleanTitle(filename: String): String {
            return filename
                .replace(Regex("(?i)\\.(md|markdown|txt)$"), "")
                .replace('_', ' ')
                .trim()
                .ifBlank { "Untitled Dispatch" }
        }

        assertEquals("Architecture Notes", cleanTitle("Architecture_Notes.md"))
        assertEquals("Weekly Dispatch 01", cleanTitle("Weekly_Dispatch_01.markdown"))
        assertEquals("Technical Specs", cleanTitle("Technical Specs.txt"))
        assertEquals("Untitled Dispatch", cleanTitle(""))
        assertEquals("Untitled Dispatch", cleanTitle(".md"))
    }

    @Test
    fun wordCountCalculation_handlesDiverseTextCorrectly() {
        fun calculateWordCount(text: String): Int {
            if (text.isBlank()) return 0
            return text.trim().split(Regex("\\s+")).filter { it.isNotBlank() }.size
        }

        assertEquals(0, calculateWordCount(""))
        assertEquals(0, calculateWordCount("    \n\t   "))
        assertEquals(1, calculateWordCount("Dispatch"))
        assertEquals(5, calculateWordCount("Tinta is a vintage reader."))
        assertEquals(4, calculateWordCount("Word1\nWord2\nWord3\nWord4"))
        assertEquals(8, calculateWordCount("Line 1\nLine 2\nLine 3\nLine 4"))
    }

    @Test
    fun snippetExtraction_cleansMarkdownSyntax() {
        fun generateSnippet(text: String): String {
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

        val rawMarkdown = """
            # The Daily Chronicle
            > An editorial quote that should be skipped
            
            This is the **opening sentence** of the dispatch.
            It contains *valuable* context for readers.
        """.trimIndent()

        val snippet = generateSnippet(rawMarkdown)
        assertFalse(snippet.startsWith("#"))
        assertFalse(snippet.startsWith(">"))
        assertTrue(snippet.contains("opening sentence"))
        assertTrue(snippet.contains("valuable"))
    }

    @Test
    fun binaryDetection_identifiesBinaryData() {
        fun isBinaryData(bytes: ByteArray): Boolean {
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

        val textBytes = "This is a normal UTF-8 markdown file with some formatting.".toByteArray(Charsets.UTF_8)
        assertFalse(isBinaryData(textBytes))

        val binaryBytesWithNull = byteArrayOf(0x50, 0x4B, 0x03, 0x04, 0x00, 0x00, 0x08)
        assertTrue(isBinaryData(binaryBytesWithNull))
    }

    @Test
    fun commonMarkParser_parsesHeadingAndParagraphCorrectly() {
        val parser = Parser.builder().build()
        val markdown = """
            # Breaking News
            
            Dispatch body text.
        """.trimIndent()

        val doc = parser.parse(markdown)
        assertNotNull(doc)
        val firstBlock = doc.firstChild
        assertTrue(firstBlock is Heading)
        assertEquals(1, (firstBlock as Heading).level)

        val secondBlock = firstBlock.next
        assertTrue(secondBlock is Paragraph)
    }
}
