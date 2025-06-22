package com.example.uastam

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val items: List<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_USER = 0
        private const val TYPE_AI = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].fromUser) TYPE_USER else TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_USER) {
            val view = inflater.inflate(R.layout.item_bubble_user, parent, false)
            UserViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_bubble_ai, parent, false)
            AIViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = items[position]
        if (holder is UserViewHolder) {
            holder.tvUserMessage.text = msg.message
        } else if (holder is AIViewHolder) {
            // Animasi hanya sekali, setelah itu text statis
            if (!msg.isAnimated) {
                animateTypewriter(holder.tvAIMessage, msg.message)
                msg.isAnimated = true
            } else {
                holder.tvAIMessage.text = msg.message
            }
        }
    }

    private fun animateTypewriter(targetView: TextView, fullText: String, interval: Long = 18L) {
        targetView.text = ""
        val handler = Handler(Looper.getMainLooper())
        var idx = 0
        val runnable = object : Runnable {
            override fun run() {
                if (idx <= fullText.length) {
                    targetView.text = fullText.substring(0, idx)
                    idx++
                    handler.postDelayed(this, interval)
                }
            }
        }
        handler.post(runnable)
    }

    override fun getItemCount(): Int = items.size

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserMessage: TextView = view.findViewById(R.id.tvUserMessage)
    }

    class AIViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAIMessage: TextView = view.findViewById(R.id.tvAIMessage)
    }
}
