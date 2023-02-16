package com.dawn.springtest.model

import java.time.LocalDateTime

data class Todo(
    val check: Boolean = false,
    val content: String = "",
    val limitTime: LocalDateTime,
    val tag: String = "",
    val ownerEmail: String ="",
)
