package com.example.uastam

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnToLogin = findViewById<Button>(R.id.btnToLogin)
        val progressBar = ProgressBar(this)
        val layout = findViewById<LinearLayout>(R.id.layoutRegister) // Tambahkan id ke LinearLayout root di XML

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password tidak boleh kosong.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!username.contains("@")) {
                Toast.makeText(this, "Username harus mengandung karakter @ (seperti email).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Tambahkan progress bar ke layout saat loading
            progressBar.isIndeterminate = true
            if (layout.indexOfChild(progressBar) == -1) layout.addView(progressBar)

            val api = BackendRetrofitClient.getClient()
            val req = RegisterRequest(username, password)
            api.register(req).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    layout.removeView(progressBar)
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        val msg = response.errorBody()?.string()
                        Toast.makeText(this@RegisterActivity, msg ?: "Gagal registrasi!", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    layout.removeView(progressBar)
                    Toast.makeText(this@RegisterActivity, "Gagal koneksi: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}
