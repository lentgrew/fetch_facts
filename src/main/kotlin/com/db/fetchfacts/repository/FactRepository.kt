package com.db.fetchfacts.repository

import com.db.fetchfacts.dto.StatisticResponse
import com.db.fetchfacts.model.Fact
import com.db.fetchfacts.model.Statistic
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.NotFoundException
import java.util.concurrent.ConcurrentHashMap

@ApplicationScoped
class FactRepository {
    private val factsCache = ConcurrentHashMap<String, Fact>()
    private val accessCounts = ConcurrentHashMap<String, Statistic>()

    fun saveFact(fact: Fact): Fact {
        factsCache[fact.shortenedUrl!!] = fact
        accessCounts[fact.shortenedUrl!!] = Statistic(0)
        return fact
    }

    fun existKey(shortenedUrl: String) = factsCache.containsKey(shortenedUrl)

    fun getFact(shortenedUrl: String): Fact {
        if(!factsCache.containsKey(shortenedUrl))
            throw NotFoundException("Fact not found")

        accessCounts.merge(shortenedUrl, Statistic(1)) { old, _ ->
            old.accessCount += 1
            old
        }
        return factsCache[shortenedUrl]!!
    }

    fun getAllFacts(): List<Fact> {
        return factsCache.values.toList()
    }

    fun getStatistics(): List<StatisticResponse> {
        return accessCounts.map { StatisticResponse(it.key, it.value.accessCount) }
    }
}