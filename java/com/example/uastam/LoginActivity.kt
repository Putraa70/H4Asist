package com.example.uastam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        // Pastikan id di XML sesuai, atau ganti jadi Button kalau bukan LinearLayout
        // val btnGoogle = findViewById<LinearLayout>(R.id.btnGoogle)

        btnLogin.setOnClickListener {
            val username = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email/username dan password tidak boleh kosong.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val api = BackendRetrofitClient.getClient()
            val req = LoginRequest(username, password)
            api.login(req).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        // Simpan username ke SharedPreferences
                        val prefs = getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
                        prefs.edit().putString("username", username).apply()
                        // Sukses login
                        Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        val msg = response.errorBody()?.string()
                        Toast.makeText(this@LoginActivity, msg ?: "Login gagal!", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Gagal koneksi: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Social login: isi logikanya nanti sesuai kebutuhan
        findViewById<LinearLayout>(R.id.btnGoogle).setOnClickListener {
            Toast.makeText(this, "Google login coming soon...", Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.btnMicrosoft).setOnClickListener {
            Toast.makeText(this, "Microsoft login coming soon...", Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.btnApple).setOnClickListener {
            Toast.makeText(this, "Apple login coming soon...", Toast.LENGTH_SHORT).show()
        }
    }
}
