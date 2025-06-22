package com.example.uastam

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {
    private lateinit var adapter: HistoryAdapter
    private val historyList = mutableListOf<HistoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        supportActionBar?.title = "Histori"

        // Siapkan RecyclerView & Adapter
        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        adapter = HistoryAdapter(historyList)
        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = adapter

        val tvEmpty = findViewById<TextView>(R.id.tvEmpty) // Tambahkan di layout XML

        // Ambil username dari SharedPreferences
        val prefs = getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        val username = prefs.getString("username", null)
        if (username.isNullOrEmpty()) {
            tvEmpty.text = "Belum login!"
            tvEmpty.visibility = View.VISIBLE
            return
        }
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }

        // Tampilkan loading sementara
        tvEmpty.text = "Memuat histori..."
        tvEmpty.visibility = View.VISIBLE

        // Panggil API history
        val api = BackendRetrofitClient.getClient()
        api.history(username).enqueue(object : Callback<List<HistoryItem>> {
            override fun onResponse(call: Call<List<HistoryItem>>, response: Response<List<HistoryItem>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: listOf()
                    if (list.isEmpty()) {
                        tvEmpty.text = "Belum ada histori."
                        tvEmpty.visibility = View.VISIBLE
                        historyList.clear()
                        adapter.notifyDataSetChanged()
                    } else {
                        tvEmpty.visibility = View.GONE
                        historyList.clear()
                        historyList.addAll(list)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    tvEmpty.text = "Gagal memuat histori (${response.code()})"
                    tvEmpty.visibility = View.VISIBLE
                }
            }


            override fun onFailure(call: Call<List<HistoryItem>>, t: Throwable) {
                tvEmpty.text = "Gagal koneksi: ${t.localizedMessage}"
                tvEmpty.visibility = View.VISIBLE
            }
        })
    }
}
