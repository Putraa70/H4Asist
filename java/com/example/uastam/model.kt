package com.example.uastam

// Untuk register
data class RegisterRequest(val username: String, val password: String)
data class RegisterResponse(val message: String)

// Untuk login
data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val message: String)

// Untuk chat GPT
data class ChatRequest(val username: String, val message: String)
data class ChatResponse(val reply: String)

// Untuk history
data class HistoryItem(val question: String, val answer: String, val at: String)
