package com.db.fetchfacts.service

import com.db.fetchfacts.model.Fact
import com.db.fetchfacts.utils.Base62
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ShortenedUrlServiceImplTest {
    private val shortenedUrlService = ShortenedUrlServiceImpl()

    @Test
    fun `given a fact when called shortenUrl then should encode fact id with collision shift`() {
        val fact = getTestFact("123")
        val collisionShift = 2L

        val result = shortenedUrlService.shortenUrl(fact, collisionShift)

        val expectedHashCode = Base62.stringToHashCode("123") + collisionShift
        val expectedEncodedUrl = Base62.encode(expectedHashCode)

        assertEquals(expectedEncodedUrl, result)
    }

    @Test
    fun `given a fact when called shortenUrl then should return the same shortened URL for the same input`() {
        val fact = getTestFact("456")

        val result1 = shortenedUrlService.shortenUrl(fact, 0L)
        val result2 = shortenedUrlService.shortenUrl(fact, 0L)

        assertEquals(result1, result2)
    }

    @Test
    fun `given  a fact when called shortenUrl should produce different results for different collision shifts`() {
        val fact = getTestFact("789")

        val result1 = shortenedUrlService.shortenUrl(fact, 0L)
        val result2 = shortenedUrlService.shortenUrl(fact, 1L)

        assertNotEquals(result1, result2)
    }

    private fun getTestFact(id: String): Fact {
        return Fact(id, "text", "source", "url", "en", "permalink", "shortenedUrl")
    }
}