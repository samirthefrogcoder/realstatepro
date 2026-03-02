package com.example.realstatepro.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.realstatepro.model.PropertyModel
import com.example.realstatepro.repository.PropertyRepo

class PropertyViewModel(private val repository: PropertyRepo) : ViewModel() {

    private val _properties = MutableLiveData<List<PropertyModel>?>()
    val properties: LiveData<List<PropertyModel>?> = _properties

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun addProperty(model: PropertyModel, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repository.addProperty(model) { success, msg ->
            _loading.value = false
            _message.value = msg
            callback(success, msg)
        }
    }

    fun getAllProperties() {
        _loading.value = true
        repository.getAllProperties { success, msg, list ->
            _loading.value = false
            if (success) {
                _properties.value = list ?: emptyList()
            } else {
                _message.value = msg
            }
        }
    }

    fun seedExampleData() {
        val examples = listOf(
            PropertyModel(title = "Luxury Villa", description = "A beautiful 5-bedroom villa with a pool.", price = "1,200,000", location = "Los Angeles, CA", imageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?q=80&w=1000&auto=format&fit=crop"),
            PropertyModel(title = "Modern Apartment", description = "High-rise apartment with city views.", price = "450,000", location = "New York, NY", imageUrl = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?q=80&w=1000&auto=format&fit=crop"),
            PropertyModel(title = "Cozy Cottage", description = "Perfect for a weekend getaway.", price = "250,000", location = "Asheville, NC", imageUrl = "https://images.unsplash.com/photo-1518780664697-55e3ad937233?q=80&w=1000&auto=format&fit=crop"),
            PropertyModel(title = "Beach House", description = "Right on the sand with private beach access.", price = "850,000", location = "Miami, FL", imageUrl = "https://images.unsplash.com/photo-1499793983690-e29da59ef1c2?q=80&w=1000&auto=format&fit=crop"),
            PropertyModel(title = "Mountain Cabin", description = "Quiet retreat in the woods.", price = "300,000", location = "Denver, CO", imageUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?q=80&w=1000&auto=format&fit=crop"),
            PropertyModel(title = "Suburban Home", description = "Spacious family home with a large backyard.", price = "500,000", location = "Dallas, TX", imageUrl = "https://images.unsplash.com/photo-1570129477492-45c003edd2be?q=80&w=1000&auto=format&fit=crop"),
            PropertyModel(title = "Downtown Studio", description = "Compact and modern, close to everything.", price = "200,000", location = "Chicago, IL", imageUrl = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?q=80&w=1000&auto=format&fit=crop"),
            PropertyModel(title = "Historic Mansion", description = "Classic architecture with modern amenities.", price = "2,500,000", location = "Charleston, SC", imageUrl = "https://images.unsplash.com/photo-1576013551627-0cc20b96c2a7?q=80&w=1000&auto=format&fit=crop"),
            PropertyModel(title = "Penthouse Suite", description = "Top floor luxury with wrap-around balcony.", price = "3,000,000", location = "San Francisco, CA", imageUrl = "https://images.unsplash.com/photo-1567496898669-ee935f5f647a?q=80&w=1000&auto=format&fit=crop"),
            PropertyModel(title = "Farmhouse", description = "Rural living with 10 acres of land.", price = "600,000", location = "Lexington, KY", imageUrl = "https://images.unsplash.com/photo-1500382017468-9049fed747ef?q=80&w=1000&auto=format&fit=crop")
        )

        examples.forEach { property ->
            addProperty(property) { _, _ -> }
        }
    }

    fun deleteProperty(id: String) {
        _loading.value = true
        repository.deleteProperty(id) { success, msg ->
            _loading.value = false
            _message.value = msg
        }
    }

    fun updateProperty(id: String, model: PropertyModel) {
        _loading.value = true
        repository.updateProperty(id, model) { success, msg ->
            _loading.value = false
            _message.value = msg
        }
    }

    fun uploadImage(uri: Uri, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repository.uploadImage(uri) { success, result ->
            _loading.value = false
            callback(success, result)
        }
    }
}
