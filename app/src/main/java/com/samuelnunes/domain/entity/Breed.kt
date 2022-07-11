package com.samuelnunes.domain.entity


data class Breed(
    val id: String,
    val image: Image?,
    val name: String,
    val description: String,
    val wikipediaName: String?
) {
    data class Image(
        val height: Int?,
        val id: String?,
        val url: String?,
        val width: Int?
    )
}