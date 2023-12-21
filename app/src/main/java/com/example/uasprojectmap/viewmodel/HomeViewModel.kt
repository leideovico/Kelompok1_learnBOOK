package com.example.uasprojectmap.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uasprojectmap.model.UserAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HomeViewModel: ViewModel() {
    //Variable to connect to Firebase
    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid

    private val database = FirebaseDatabase.getInstance()
    private val userRef = uid?.let { database.getReference("users").child(it) }

    private val storage = FirebaseStorage.getInstance()
    private val storageRefUser = storage.getReference("users").child(uid!!)

    //Variable to store and update data in Firebase
    private val _namaLengkap = MutableLiveData<String>()
    val namaLengkap: LiveData<String> = _namaLengkap

    private val _umur = MutableLiveData<Int>()
    val umur: LiveData<Int> = _umur

    private val _imageUri = MutableLiveData<String>()
    val imageUri: LiveData<String> = _imageUri

    //Method to run when this ViewModel is used
    init {
        fetchDataFromFirebase()
    }

    //Fetch data of current user into User
    private suspend fun fetchCurrentUserFromFirebase(): UserAccount? = withContext(Dispatchers.IO) {
        return@withContext suspendCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userAccount = snapshot.getValue(UserAccount::class.java)
                    continuation.resume(userAccount)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            }
            userRef?.addListenerForSingleValueEvent(listener)
        }
    }

    //Extract nama lengkap, image, dan umur
    private fun fetchDataFromFirebase() {
        viewModelScope.launch {
            try {
                val user = fetchCurrentUserFromFirebase()
                if (user != null) {
                    _namaLengkap.value = user.nama_lengkap
                    _imageUri.value = user.user_url_img

                    val tahunLahir = extractYearFromDateString(user.tanggal_lahir)
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                    _umur.value = currentYear - tahunLahir!!
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    //Extract year from date
    private fun extractYearFromDateString(dateString: String): Int? {
        return try {
            val dateFormat = SimpleDateFormat("d/MM/yy", Locale.getDefault())
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.get(Calendar.YEAR)
        } catch (e: Exception) {
            // Jika parsing gagal, kembalikan null
            null
        }
    }
}