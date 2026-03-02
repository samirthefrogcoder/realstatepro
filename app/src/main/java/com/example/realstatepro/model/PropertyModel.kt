package com.example.realstatepro.model

data class PropertyModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val location: String = "",
    val imageUrl: String = "",
    val ownerId: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "description" to description,
            "price" to price,
            "location" to location,
            "imageUrl" to imageUrl,
            "ownerId" to ownerId
        )
    }
}
