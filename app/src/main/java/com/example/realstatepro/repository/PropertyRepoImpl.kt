package com.example.realstatepro.repository

import android.net.Uri
import com.example.realstatepro.model.PropertyModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class PropertyRepoImpl : PropertyRepo {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.getReference("Properties")
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    override fun addProperty(model: PropertyModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key ?: ""
        val propertyWithId = model.copy(id = id)
        ref.child(id).setValue(propertyWithId).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Property added successfully")
            } else {
                callback(false, it.exception?.message ?: "Error adding property")
            }
        }
    }

    override fun updateProperty(id: String, model: PropertyModel, callback: (Boolean, String) -> Unit) {
        ref.child(id).updateChildren(model.toMap()).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Property updated successfully")
            } else {
                callback(false, it.exception?.message ?: "Error updating property")
            }
        }
    }

    override fun deleteProperty(id: String, callback: (Boolean, String) -> Unit) {
        ref.child(id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Property deleted successfully")
            } else {
                callback(false, it.exception?.message ?: "Error deleting property")
            }
        }
    }

    override fun getPropertyById(id: String, callback: (Boolean, String, PropertyModel?) -> Unit) {
        ref.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val property = snapshot.getValue(PropertyModel::class.java)
                callback(true, "Fetched successfully", property)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getAllProperties(callback: (Boolean, String, List<PropertyModel>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<PropertyModel>()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        data.getValue(PropertyModel::class.java)?.let { list.add(it) }
                    }
                    callback(true, "Fetched successfully", list)
                } else {
                    callback(true, "No properties found", emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun uploadImage(imageUri: Uri, callback: (Boolean, String) -> Unit) {
        val fileName = UUID.randomUUID().toString()
        val imageRef = storage.reference.child("PropertyImages/$fileName")
        
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl
                    .addOnSuccessListener { url ->
                        callback(true, url.toString())
                    }
                    .addOnFailureListener {
                        callback(false, "Failed to get download URL: ${it.message}")
                    }
            }
            .addOnFailureListener {
                callback(false, "Upload failed: ${it.message}")
            }
    }
}
