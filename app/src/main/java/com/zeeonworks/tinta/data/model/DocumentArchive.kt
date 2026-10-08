package com.zeeonworks.tinta.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DocumentArchive(
    val id: String = "",
    val uriString: String,
    val title: String,
    val lastOpenedEpoch: Long,
    val wordCount: Int,
    val snippet: String,
    val localFilePath: String = ""
)
