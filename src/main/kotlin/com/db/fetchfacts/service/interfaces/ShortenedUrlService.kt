package com.db.fetchfacts.service.interfaces

import com.db.fetchfacts.model.Fact

interface ShortenedUrlService {
    fun shortenUrl(fact: Fact, collisionShift: Long = 0L): String
}