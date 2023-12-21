package com.example.uasprojectmap.model

//Model atau template data class untuk Journal
//Inherit dari Publication
data class Journal(
    override val id: String = "",
    override val title: String="",
    override val author: String="",
    override val description: String="",
    override val url_img: String="",
    override val url_pdf: String=""
): Publication(id, title, author, description, url_img, url_pdf)
