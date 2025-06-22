package com.example.uastam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val items: List<HistoryItem>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvQ: TextView = view.findViewById(R.id.tvQ)
        val tvA: TextView = view.findViewById(R.id.tvA)
        val tvT: TextView = view.findViewById(R.id.tvT)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvQ.text = "Q: ${item.question}"
        holder.tvA.text = "A: ${item.answer}"
        holder.tvT.text = item.at
    }

    override fun getItemCount() = items.size
}
