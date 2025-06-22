package com.example.uastam

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        // Sembunyikan action bar default agar header custom tampil
        supportActionBar?.hide()

        // Tombol back (pastikan ID sama dengan XML!)
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Tombol tambah project
        findViewById<Button>(R.id.btnTambah).setOnClickListener {
            Toast.makeText(this, "Tambah Project diklik!", Toast.LENGTH_SHORT).show()

        }
    }
}
