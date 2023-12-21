package com.example.uasprojectmap

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uasprojectmap.model.UserAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var namaEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var tanggalLahirEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var password2EditText: EditText
    private lateinit var registerButton: Button

    private var namaLengkap: String? = null
    private var tahunLahir: Int? = null
    private var email: String? = null
    private var password: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi elemen UI
        namaEditText = findViewById(R.id.namaEditText)
        emailEditText = findViewById(R.id.emailEditText)
        tanggalLahirEditText = findViewById(R.id.tanggalLahirEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        password2EditText = findViewById(R.id.password2EditText)
        val sudahAdaTextView: TextView = findViewById(R.id.SudahAdaTextView)
        registerButton = findViewById(R.id.registerButton)

        //Inisialisasi Firebase Auth dan Realtime Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Set OnClickListener untuk menampilkan DatePickerDialog
        tanggalLahirEditText.setOnClickListener {
            showDatePickerDialog()
        }

        tanggalLahirEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showBubbleWarning()
            }
        }

        sudahAdaTextView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //cek registerButton diklik atau tidak
        registerButton.setOnClickListener {
            if (isRegistrationDataComplete()) {
                // Simpan nama lengkap
                namaLengkap = namaEditText.text.toString()
                //Simpan email
                email = emailEditText.text.toString()
                // Simpan tahun lahir
                tahunLahir = extractYearFromDateString(tanggalLahirEditText.text.toString())

                // Implementasi logika registrasi di sini
                if (tahunLahir != null && isPasswordValid()) {
                    //Simpan password setelah konfirmasi password sama dengan password yang dimasukkan
                    password = passwordEditText.text.toString()

                    // Lakukan registrasi
                    lifecycleScope.launch(Dispatchers.IO) {
                        registerUser()
                    }

                } else {
                    // Tampilkan pesan jika parsing tanggal lahir gagal atau kata sandi tidak cocok
                    Toast.makeText(this, "Format tanggal lahir tidak valid atau kata sandi tidak sama", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Tampilkan pesan jika identitas belum lengkap
                Toast.makeText(this, "Harap isi semua identitas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Methods for calendar
    private fun showBubbleWarning() {
        Toast.makeText(this, "Klik dua kali untuk mengakses calendar", Toast.LENGTH_SHORT).show()
    }

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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                val dateFormat = SimpleDateFormat("d/MM/yy", Locale.getDefault())
                tanggalLahirEditText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Batasi tanggal yang dapat dipilih ke tanggal sekarang atau sebelumnya
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    //Check email yang dimasukkan sesuai format atau tidak
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //Methods for check inputted password and complete form
    private fun isPasswordValid(): Boolean {
        val password = passwordEditText.text.toString()
        val confirmPassword = password2EditText.text.toString()

        return password == confirmPassword
    }

    //Cek data yang dimasukkan lengkap atau tidak
    private fun isRegistrationDataComplete(): Boolean {
        val nama = namaEditText.text.toString()
        val tanggalLahir = tanggalLahirEditText.text.toString()
        val email = emailEditText.text.toString() // Gantilah dengan ID yang sesuai

        // Tambahkan identitas lain yang perlu diisi
        val isComplete = nama.isNotEmpty() && tanggalLahir.isNotEmpty() && email.isNotEmpty()
        if (!isComplete) {
            // Tampilkan pesan jika identitas belum lengkap
            Toast.makeText(this, "Harap isi semua identitas", Toast.LENGTH_SHORT).show()
        }

        if(!isValidEmail(email)){
            Toast.makeText(this, "Format email salah. Contoh format yang benar: nama@email.com'", Toast.LENGTH_SHORT).show()
        }

        return isComplete
    }

    //Method untuk register user
    private suspend fun registerUser(){
        val userRef = database.getReference("users")
        var uid = ""
        val tanggalLahir = tanggalLahirEditText.text.toString()

        try {
            //Buat akun user di Firebase Auth
            auth.createUserWithEmailAndPassword(email!!, password!!).await()

            //Buat akun user di Firebase Realtime Database
            //Data yang disimpan berupa UID Akun di Auth, Nama Lengkap, Tanggal Lahir,
            // No Telpon (inisialisasi awal kosong), dan Pelajaran Favorit (inisialisasi awal kosong)
            uid = auth.currentUser?.uid ?: ""

            val user = UserAccount(uid, namaLengkap!!, tanggalLahir, "", "", "")
            userRef.child(uid).setValue(user)

            //Tampilkan success message dan dijalankan di Main Thread
            runOnUiThread {
                Toast.makeText(this@RegisterActivity, "Registrasi sukses", Toast.LENGTH_SHORT).show()
            }
            //Pindah ke MainAppActivity
            val intent = Intent(this@RegisterActivity, MainAppActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception){
            runOnUiThread{
                Toast.makeText(this@RegisterActivity, "Registrasi gagal. Silahkan registrasi kembali.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
