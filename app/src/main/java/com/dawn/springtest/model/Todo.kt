package com.dawn.springtest.model

import org.intellij.lang.annotations.Identifier
import java.time.LocalDateTime

data class Todo(
    val id: String="",
    val content: String = "",
    val limitDateTime: String = "",
    val tag: String = "",
    val ownerEmail: String = "",
    val finish: String=""
)
