package com.example.uasprojectmap.model

//Model atau template data class untuk Publication
//Publication merupakan parent dari data class Book dan Journal
open class Publication(
    open val id: String,
    open val title: String,
    open val author: String,
    open val description: String,
    open val url_img: String,
    open val url_pdf: String
)