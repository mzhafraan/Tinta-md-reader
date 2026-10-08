package com.zeeonworks.tinta.ui.reader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zeeonworks.tinta.data.repository.BinaryContentException
import com.zeeonworks.tinta.data.repository.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ReaderUiState(
    val archiveId: String = "",
    val uriString: String = "",
    val title: String = "",
    val content: String = "",
    val wordCount: Int = 0,
    val readingTimeMinutes: Int = 1,
    val dateStamp: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isMissingFile: Boolean = false
)

class ReaderViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: DocumentRepository = DocumentRepository(application)

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    fun loadDocument(identifier: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                archiveId = identifier,
                isLoading = true,
                errorMessage = null,
                isMissingFile = false
            )

            try {
                val result = repository.loadDocumentByIdOrUri(identifier)

                result.onSuccess { docResult ->
                    val readingTime = (docResult.archive.wordCount / 200).coerceAtLeast(1)
                    val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.US)
                    val dateFormatted = sdf.format(Date(docResult.archive.lastOpenedEpoch))

                    _uiState.value = ReaderUiState(
                        archiveId = docResult.archive.id,
                        uriString = docResult.archive.uriString,
                        title = docResult.archive.title,
                        content = docResult.content,
                        wordCount = docResult.archive.wordCount,
                        readingTimeMinutes = readingTime,
                        dateStamp = dateFormatted,
                        isLoading = false,
                        errorMessage = null,
                        isMissingFile = false
                    )
                }.onFailure { ex ->
                    handleFailure(identifier, ex)
                }
            } catch (ex: Exception) {
                handleFailure(identifier, ex)
            }
        }
    }

    private fun handleFailure(identifier: String, ex: Throwable) {
        val isMissing = ex is FileNotFoundException || ex is SecurityException
        val message = when {
            ex is BinaryContentException -> ex.message ?: "Unable to decode textual contents of this dispatch."
            isMissing -> "The source file was moved, deleted, or access was revoked."
            else -> ex.localizedMessage ?: "Failed to open dispatch."
        }

        _uiState.value = _uiState.value.copy(
            archiveId = identifier,
            isLoading = false,
            errorMessage = message,
            isMissingFile = isMissing
        )
    }

    fun removeMissingFileFromArchives(onComplete: () -> Unit) {
        viewModelScope.launch {
            val id = _uiState.value.archiveId.ifBlank { _uiState.value.uriString }
            if (id.isNotBlank()) {
                repository.removeArchiveById(id)
            }
            onComplete()
        }
    }
}
