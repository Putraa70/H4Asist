package com.example.uastam

data class ChatMessage(
    val message: String,
    val fromUser: Boolean,
    var isAnimated: Boolean = false
)
