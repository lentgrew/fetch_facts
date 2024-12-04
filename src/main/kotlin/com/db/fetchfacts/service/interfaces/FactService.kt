package com.db.fetchfacts.service.interfaces

import com.db.fetchfacts.dto.CachedFact
import com.db.fetchfacts.dto.ShortenedFact
import com.db.fetchfacts.dto.StatisticResponse
import io.smallrye.mutiny.Uni

interface FactService {
    fun fetchAndShortenFact(): Uni<ShortenedFact>
    fun getFactByShortenedUrl(shortenedUrl: String): CachedFact
    fun getOriginalPermalink(shortenedUrl: String): String
    fun getAllFacts(): List<CachedFact>
    fun getStatistics(): List<StatisticResponse>
}