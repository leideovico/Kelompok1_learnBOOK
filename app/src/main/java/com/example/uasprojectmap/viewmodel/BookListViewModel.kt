package com.example.uasprojectmap.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uasprojectmap.model.Book
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

class BookListViewModel: ViewModel() {
    //Variabel to connect to Firebase
    private val database = FirebaseDatabase.getInstance()
    private val bookRef = database.getReference("books")

    //variable to store fetched bookList
    var bookList by mutableStateOf<List<Book>>(emptyList())

    //Method to run when this ViewModel is used
    init {
        fetchDataFromFirebase()
    }

    //Fetch data of current user into book list
    private suspend fun fetchBooksFromFirebase(): List<Book> = withContext(Dispatchers.IO) {
        return@withContext suspendCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempBookList = mutableListOf<Book>()
                    for (dataSnap in snapshot.children) {
                        val booksItem = dataSnap.getValue(Book::class.java)
                        booksItem?.let {
                            tempBookList.add(it)
                        }
                    }
                    continuation.resume(tempBookList)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            }
            bookRef.addListenerForSingleValueEvent(listener)
        }
    }

    //Store fetched book list intp bookList
    private fun fetchDataFromFirebase() {
        viewModelScope.launch {
            try {
                val books = fetchBooksFromFirebase()
                bookList = books
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    //Method to find book by id
    fun findBookById(id: String): Book? {
        return bookList.find { it.id == id }
    }
}