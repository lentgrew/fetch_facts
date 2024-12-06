package com.db.fetchfacts.service

import com.db.fetchfacts.client.FactsClient
import com.db.fetchfacts.dto.CachedFact
import com.db.fetchfacts.dto.ShortenedFact
import com.db.fetchfacts.dto.StatisticResponse
import com.db.fetchfacts.model.Fact
import com.db.fetchfacts.repository.FactRepository
import com.db.fetchfacts.service.FactServiceImpl.Companion.LANGUAGE
import com.db.fetchfacts.service.interfaces.ShortenedUrlService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.smallrye.mutiny.Uni
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FactServiceImplTest {

    private val factsClient: FactsClient = mockk()
    private val shortenedUrlService: ShortenedUrlService = mockk()
    private val factRepository: FactRepository = mockk()
    private val factService: FactServiceImpl = FactServiceImpl(factsClient, shortenedUrlService, factRepository)

    @Test
    fun `when called fetchAndShortenFact then should fetch a fact, shorten its URL, and return a ShortenedFact`() {
        val shortenedUrl = "url"
        val fact = getTestFact(null)

        every { factsClient.getRandomFact(LANGUAGE) } returns Uni.createFrom().item(fact)
        every { shortenedUrlService.shortenUrl(fact, 0L) } returns shortenedUrl
        every { factRepository.existKey(shortenedUrl) } returns false
        every { factRepository.saveFact(any()) } answers { firstArg() }

        val result = factService.fetchAndShortenFact().await().indefinitely()

        verify(exactly = 1) { factsClient.getRandomFact(any()) }
        verify(exactly = 1) { shortenedUrlService.shortenUrl(any(), eq(0L)) }
        verify(exactly = 1) { factRepository.saveFact(any()) }

        assertEquals(ShortenedFact(fact.text, shortenedUrl), result)
    }

    @Test
    fun `when called getFactByShortenedUrl then should return a CachedFact`() {
        val shortenedUrl = "url"
        val fact = getTestFact(shortenedUrl)
        every { factRepository.getFact(shortenedUrl) } returns fact

        val result = factService.getFactByShortenedUrl(shortenedUrl)

        verify(exactly = 1) { factRepository.getFact(shortenedUrl) }
        assertEquals(CachedFact(fact.text, fact.permalink), result)
    }

    @Test
    fun `given some fact when called getAllFacts then should return a list of CachedFact`() {
        val facts = listOf(
                getTestFact("url1"),
                getTestFact("url2")
        )
        every { factRepository.getAllFacts() } returns facts
        val expectedFacts = facts.map { CachedFact(it.text, it.permalink) }

        val result = factService.getAllFacts()

        verify(exactly = 1) { factRepository.getAllFacts() }
        assertEquals(2, result.size)
        assertTrue(expectedFacts.containsAll(result))

    }

    @Test
    fun `when called getOriginalPermalink then should return the original permalink for a shortened URL`() {
        val fact = getTestFact("url")
        every { factRepository.getFact("url") } returns fact

        val result = factService.getOriginalPermalink("url")

        verify(exactly = 1) { factRepository.getFact("url") }
        assertEquals(fact.permalink, result)
    }

    @Test
    fun `getStatistics should return a list of StatisticResponse`() {
        val statistics = listOf(
                StatisticResponse("url1", 10),
                StatisticResponse("url2", 5)
        )
        every { factRepository.getStatistics() } returns statistics

        val result = factService.getStatistics()

        verify(exactly = 1) { factRepository.getStatistics() }
        assertTrue(statistics.containsAll(result))
    }

    private fun getTestFact(shortenedUrl: String?, text: String = "text", permalink: String = "permalink"): Fact {
        return Fact("id", text, "source", "url", "en", permalink, shortenedUrl)
    }
}