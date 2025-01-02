package com.db.fetchfacts.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class Fact(
    val id: String,
    val text: String,
    val source: String?,
    val sourceUrl: String?,
    val language: String,
    val permalink: String,
    @set:JsonIgnore
    @get:JsonIgnore
    var shortenedUrl: String? = null
)