package com.example.uastam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private val chatList = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val rvChat = findViewById<RecyclerView>(R.id.rvChat)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<ImageButton>(R.id.btnSend)
        val tvPrompt = findViewById<TextView>(R.id.tvPrompt)

        // ProgressBar loading (optional, tambahkan ke layout jika ingin)
        val progressBar = ProgressBar(this)
        val mainLayout = findViewById<View>(R.id.main) ?: rvChat // gunakan layout utama

        // Ambil username dari SharedPreferences
        val prefs = getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        val username = prefs.getString("username", null)
        if (username.isNullOrEmpty()) {
            // Jika user belum login, kembali ke login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Buka drawer saat klik hamburger
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }


        // Setup RecyclerView
        adapter = ChatAdapter(chatList)
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = adapter

        fun updatePrompt() {
            if (chatList.isEmpty()) {
                tvPrompt.visibility = View.VISIBLE
                rvChat.visibility = View.GONE
            } else {
                tvPrompt.visibility = View.GONE
                rvChat.visibility = View.VISIBLE
            }
        }
        updatePrompt()

        btnSend.setOnClickListener {
            val msg = etMessage.text.toString().trim()
            if (msg.isEmpty()) return@setOnClickListener

            // Tampilkan pesan user di bubble chat
            chatList.add(ChatMessage(msg, true))
            adapter.notifyItemInserted(chatList.size - 1)
            rvChat.scrollToPosition(chatList.size - 1)
            etMessage.text.clear()
            updatePrompt()

            // ProgressBar (optional)
            progressBar.isIndeterminate = true
            if (mainLayout is LinearLayout && mainLayout.indexOfChild(progressBar) == -1) {
                mainLayout.addView(progressBar)
            }

            // Kirim ke backend (Retrofit)
            val api = BackendRetrofitClient.getClient()
            val req = ChatRequest(username, msg)
            api.chat(req).enqueue(object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    // Hapus progress bar
                    if (mainLayout is LinearLayout) mainLayout.removeView(progressBar)
                    if (response.isSuccessful) {
                        val reply = response.body()?.reply ?: "AI tidak membalas."
                        chatList.add(ChatMessage(reply, false))
                        adapter.notifyItemInserted(chatList.size - 1)
                        rvChat.scrollToPosition(chatList.size - 1)
                    } else {
                        chatList.add(ChatMessage("AI gagal membalas (${response.code()})", false))
                        adapter.notifyItemInserted(chatList.size - 1)
                    }
                    updatePrompt()
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    if (mainLayout is LinearLayout) mainLayout.removeView(progressBar)
                    chatList.add(ChatMessage("Gagal koneksi ke AI: ${t.localizedMessage}", false))
                    adapter.notifyItemInserted(chatList.size - 1)
                    updatePrompt()
                }
            })
        }

        // Handle klik menu drawer
        navigationView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_dashboard -> {
                    // Sudah di halaman utama, tidak perlu pindah
                    drawerLayout.closeDrawers()
                }
                R.id.menu_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    drawerLayout.closeDrawers()
                }
                R.id.menu_project -> {
                    startActivity(Intent(this, ProjectActivity::class.java))
                    drawerLayout.closeDrawers()
                }
                R.id.menu_detail -> {
                    startActivity(Intent(this, DetailActivity::class.java))
                    drawerLayout.closeDrawers()
                }
                R.id.menu_logout -> {
                    prefs.edit().clear().apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            true
        }
    }
}
