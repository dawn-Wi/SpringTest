package com.dawn.springtest.model

import java.time.LocalDateTime

data class Todo(
    val content: String = "",
    val limitDateTime: String = "",
    val tag: String = "",
    val ownerEmail: String = "",
)
