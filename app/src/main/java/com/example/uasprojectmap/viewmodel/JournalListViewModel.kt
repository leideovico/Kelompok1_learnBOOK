package com.example.uasprojectmap.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uasprojectmap.model.Journal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class JournalListViewModel: ViewModel() {
    //Variabel to connect to Firebase
    private val database = FirebaseDatabase.getInstance()
    private val journalRef = database.getReference("journals")

    //variable to store fetched journal list
    var journalList by mutableStateOf<List<Journal>>(emptyList())

    //Method to run when this ViewModel is used
    init {
        fetchDataFromFirebase()
    }

    //Fetch data of current user into journal list
    private suspend fun fetchJournalsFromFirebase(): List<Journal> = withContext(Dispatchers.IO) {
        return@withContext suspendCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempJournalList = mutableListOf<Journal>()
                    for (dataSnap in snapshot.children) {
                        val journalItem = dataSnap.getValue(Journal::class.java)
                        journalItem?.let {
                            tempJournalList.add(it)
                        }
                    }
                    continuation.resume(tempJournalList)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            }
            journalRef.addListenerForSingleValueEvent(listener)
        }
    }

    //Store fetched book list into journalList
    private fun fetchDataFromFirebase() {
        viewModelScope.launch {
            try {
                val journals = fetchJournalsFromFirebase()
                journalList = journals
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    //Method to find journal by id
    fun findJournalById(id: String): Journal? {
        return journalList.find { it.id == id }
    }
}