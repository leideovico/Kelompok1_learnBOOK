package com.example.uasprojectmap

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

//Activity yang dijalankan ketika aplikasi pertama dijalankan. Activity untuk login.
class MainActivity : AppCompatActivity() {
    //Variable untuk konek ke FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Hubungkan MainActivity.kt dengan activity_main
        setContentView(R.layout.activity_main)

        //Inisialisasi auth
        auth = FirebaseAuth.getInstance()

        // Inisialisasi elemen UI
        val belumDaftarTextView: TextView = findViewById(R.id.belumDaftarTextView)
        val loginButton: Button = findViewById(R.id.loginButton)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val emailInputLayout: TextInputLayout = findViewById(R.id.emailInputLayout)
        val passwordInputLayout: TextInputLayout = findViewById(R.id.passwordInputLayout)

        //Cek belum daftar diklik atau tidak
        belumDaftarTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //Cek login diklik atau tidak
        loginButton.setOnClickListener {
            // Mengambil nilai dari input box
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Memeriksa apakah kedua input sudah diisi
            if (email.isBlank() || password.isBlank()) {
                // Jika ada input yang kosong, tampilkan pesan toast
                Toast.makeText(this, "Tolong isi semua identitas!", Toast.LENGTH_SHORT).show()

                // Tandai input yang kosong dengan merubah outline box menjadi merah
                if (email.isBlank()) {
                    emailInputLayout.boxStrokeColor = resources.getColor(android.R.color.holo_red_dark)
                }

                if (password.isBlank()) {
                    passwordInputLayout.boxStrokeColor = resources.getColor(android.R.color.holo_red_dark)
                }
            } else {
                if (!isValidEmail(email)){
                    Toast.makeText(this, "Format email salah. Contoh format yang benar: nama@email.com'", Toast.LENGTH_SHORT).show()
                }
                else{
                    // Jika kedua input sudah diisi
                    // Implementasi logika login di sini
                    loginButton.setOnClickListener{
                        // Reset warna outline box ke warna semula
                        emailInputLayout.boxStrokeColor = resources.getColor(R.color.colorPrimary)
                        passwordInputLayout.boxStrokeColor = resources.getColor(R.color.colorPrimary)

                        loginUser(auth, email, password)
                    }
                }
            }
        }
    }
    //Check email yang dimasukkan sesuai format atau tidak
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //Method untuk login user
    private fun loginUser(auth: FirebaseAuth, email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        baseContext,
                        "Login berhasil.",
                        Toast.LENGTH_SHORT,
                    ).show()

                    val user = auth.currentUser

                    val intent = Intent(this, MainAppActivity::class.java)
                    intent.putExtra("CURRENT_USER", user)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Login gagal. Silakan masukkan kembali email dan password anda.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}