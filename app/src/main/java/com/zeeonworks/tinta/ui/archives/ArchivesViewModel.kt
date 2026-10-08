package com.zeeonworks.tinta.ui.archives

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zeeonworks.tinta.data.model.DocumentArchive
import com.zeeonworks.tinta.data.repository.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ArchivesUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null
)

class ArchivesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: DocumentRepository = DocumentRepository(application)

    val archives: StateFlow<List<DocumentArchive>> = repository.archivesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(ArchivesUiState())
    val uiState: StateFlow<ArchivesUiState> = _uiState.asStateFlow()

    fun onDocumentSelected(uri: Uri, onNavigateToReader: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.openAndCacheDocument(uri)
            _uiState.value = _uiState.value.copy(isLoading = false)

            result.onSuccess { docResult ->
                onNavigateToReader(docResult.archive.id)
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = exception.localizedMessage ?: "Failed to procure document."
                )
            }
        }
    }

    fun deleteArchive(archive: DocumentArchive) {
        viewModelScope.launch {
            repository.removeArchive(archive)
            _uiState.value = _uiState.value.copy(
                infoMessage = "Removed \"${archive.title}\" from archives."
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, infoMessage = null)
    }
}
