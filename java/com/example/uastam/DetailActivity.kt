package com.example.uastam

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Sembunyikan action bar default supaya pakai header custom
        supportActionBar?.hide()

        // Tombol kembali (pastikan id sama di XML: @+id/btnBack)
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
