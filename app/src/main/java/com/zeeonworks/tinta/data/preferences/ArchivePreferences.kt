package com.zeeonworks.tinta.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zeeonworks.tinta.data.model.DocumentArchive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tinta_archives_preferences")

class ArchivePreferences(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    companion object {
        private val KEY_SAVED_ARCHIVES = stringPreferencesKey("saved_archives_json")
    }

    val archivesFlow: Flow<List<DocumentArchive>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val jsonString = preferences[KEY_SAVED_ARCHIVES] ?: "[]"
            try {
                val list = json.decodeFromString<List<DocumentArchive>>(jsonString)
                // Ensure every archive has a non-empty ID
                list.map { item ->
                    if (item.id.isBlank()) {
                        val computedId = item.uriString.hashCode().toUInt().toString(16)
                        item.copy(id = computedId)
                    } else {
                        item
                    }
                }
            } catch (e: Exception) {
                emptyList()
            }
        }

    suspend fun getAllArchives(): List<DocumentArchive> {
        return archivesFlow.first()
    }

    suspend fun getArchive(idOrUri: String): DocumentArchive? {
        return getAllArchives().firstOrNull { it.id == idOrUri || it.uriString == idOrUri }
    }

    suspend fun saveOrUpdateArchive(archive: DocumentArchive) {
        val archiveToSave = if (archive.id.isBlank()) {
            val computedId = archive.uriString.hashCode().toUInt().toString(16)
            archive.copy(id = computedId)
        } else {
            archive
        }

        context.dataStore.edit { preferences ->
            val jsonString = preferences[KEY_SAVED_ARCHIVES] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<DocumentArchive>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }.toMutableList()

            // Remove if entry already exists (by id or uriString)
            currentList.removeAll { it.id == archiveToSave.id || it.uriString == archiveToSave.uriString }

            // Prepend updated archive to top of list
            currentList.add(0, archiveToSave)

            preferences[KEY_SAVED_ARCHIVES] = json.encodeToString(currentList)
        }
    }

    suspend fun removeArchive(idOrUri: String) {
        context.dataStore.edit { preferences ->
            val jsonString = preferences[KEY_SAVED_ARCHIVES] ?: "[]"
            val currentList = try {
                json.decodeFromString<List<DocumentArchive>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }.toMutableList()

            currentList.removeAll { it.id == idOrUri || it.uriString == idOrUri }

            preferences[KEY_SAVED_ARCHIVES] = json.encodeToString(currentList)
        }
    }

    suspend fun clearAllArchives() {
        context.dataStore.edit { preferences ->
            preferences[KEY_SAVED_ARCHIVES] = "[]"
        }
    }
}
