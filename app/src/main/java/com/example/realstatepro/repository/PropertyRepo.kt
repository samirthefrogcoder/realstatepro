package com.example.realstatepro.repository

import android.net.Uri
import com.example.realstatepro.model.PropertyModel

interface PropertyRepo {
    fun addProperty(model: PropertyModel, callback: (Boolean, String) -> Unit)
    fun updateProperty(id: String, model: PropertyModel, callback: (Boolean, String) -> Unit)
    fun deleteProperty(id: String, callback: (Boolean, String) -> Unit)
    fun getPropertyById(id: String, callback: (Boolean, String, PropertyModel?) -> Unit)
    fun getAllProperties(callback: (Boolean, String, List<PropertyModel>?) -> Unit)
    fun uploadImage(imageUri: Uri, callback: (Boolean, String) -> Unit)
}
