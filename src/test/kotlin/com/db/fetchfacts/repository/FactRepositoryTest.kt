package com.db.fetchfacts.repository

import com.db.fetchfacts.model.Fact
import jakarta.ws.rs.NotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FactRepositoryTest {
    private lateinit var factRepository: FactRepository

    @BeforeEach
    fun setUp() {
        factRepository = FactRepository()
    }

    @Test
    fun `given a fact when called saveFact then should save a fact and initialize statistics`() {
        val fact = getTestFact("url")
        val savedFact = factRepository.saveFact(fact)

        assertEquals(fact, savedFact)
        assertTrue(factRepository.existKey("url"))
    }

    @Test
    fun `given a fact when called existKey then should return true if key exists and false otherwise`() {
        val fact = getTestFact("url")
        factRepository.saveFact(fact)

        assertTrue(factRepository.existKey("url"))
        assertFalse(factRepository.existKey("url2"))
    }

    @Test
    fun `given a fact when called getFact then should return the fact and increment access count`() {
        val fact = getTestFact("url")
        factRepository.saveFact(fact)

        val retrievedFact = factRepository.getFact("url")
        assertEquals(fact, retrievedFact)

        val stats = factRepository.getStatistics()
        val statForFact = stats.first { it.shortenedUrl == "url" }

        assertNotNull(statForFact)
        assertEquals(1, statForFact.accessCount)
    }

    @Test
    fun `given nothing when called getFact should throw NotFoundException if fact does not exist`() {
        val exception = assertThrows(NotFoundException::class.java) {
            factRepository.getFact("nonexistentUrl")
        }

        assertEquals("Fact not found", exception.message)
    }

    @Test
    fun `given facts when called getAllFacts then should return a list of cached facts`() {
        val fact1 = getTestFact("url1")
        val fact2 = getTestFact("url2")

        factRepository.saveFact(fact1)
        factRepository.saveFact(fact2)

        val allFacts = factRepository.getAllFacts()
        val expectedFacts = listOf(fact1, fact2)

        assertTrue(expectedFacts.containsAll(allFacts))
    }

    @Test
    fun `given some facts when called getStatistics then should return correct access counts`() {
        val fact1 = getTestFact("url1")
        val fact2 = getTestFact("url2")

        factRepository.saveFact(fact1)
        factRepository.saveFact(fact2)

        factRepository.getFact("url1")
        factRepository.getFact("url1")
        factRepository.getFact("url2")

        val stats = factRepository.getStatistics()

        assertEquals(2, stats.first() { it.shortenedUrl == "url1" }.accessCount)
        assertEquals(1, stats.first() { it.shortenedUrl == "url2" }.accessCount)
    }

    private fun getTestFact(shortenedUrl: String = "shortenedUrl", text: String = "", permalink: String = ""): Fact {
        return Fact("id", text, "source", "url", "en", permalink, shortenedUrl = shortenedUrl)
    }
}