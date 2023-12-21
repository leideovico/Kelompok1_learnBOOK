package com.example.uasprojectmap.model

import java.util.Date

//Model atau template data class untuk Akun User
data class UserAccount(
    val user_id: String = "",
    val nama_lengkap: String = "",
    val tanggal_lahir: String = "",
    val no_telpon: String = "",
    val fav_subject: String = "",
    val user_url_img: String = ""
)
