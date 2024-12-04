package com.db.fetchfacts.service

import com.db.fetchfacts.client.FactsClient
import com.db.fetchfacts.dto.CachedFact
import com.db.fetchfacts.dto.ShortenedFact
import com.db.fetchfacts.dto.StatisticResponse
import com.db.fetchfacts.model.Fact
import com.db.fetchfacts.repository.FactRepository
import com.db.fetchfacts.service.interfaces.FactService
import com.db.fetchfacts.service.interfaces.ShortenedUrlService
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger


@ApplicationScoped
class FactServiceImpl(
        @RestClient private val factsClient: FactsClient,
        private val shortenedUrlService: ShortenedUrlService,
        private val factRepository: FactRepository
) : FactService {
    private val log: Logger = Logger.getLogger(FactServiceImpl::class.java)
    private val language: String = "en"

    override fun fetchAndShortenFact(): Uni<ShortenedFact> {
        log.info("Fetch random fact for language: $language")
        return factsClient.getRandomFact(language)
                .onFailure().invoke { throwable ->
                    log.error("Error occurred while fetching fact: $throwable")
                }
                .onFailure().retry().atMost(3)
                .onItem().transform { fact ->
                    shortenUrl(fact)
                    ShortenedFact(fact.text, fact.shortenedUrl!!)
                }
    }

    override fun getFactByShortenedUrl(shortenedUrl: String): CachedFact {
        return factRepository.getFact(shortenedUrl).let {
            CachedFact(it.text, it.permalink)
        }
    }

    override fun getAllFacts(): List<CachedFact> {
        return factRepository.getAllFacts()
    }

    override fun getOriginalPermalink(shortenedUrl: String): String {
        return factRepository.getFact(shortenedUrl).permalink
    }

    override fun getStatistics(): List<StatisticResponse> {
        return factRepository.getStatistics()
    }
    private fun shortenUrl(fact: Fact, collisionShift: Long = 0L): Fact {
        val shortenedUrl = shortenedUrlService.shortenUrl(fact)
        if (factRepository.existKey(shortenedUrl))
            return shortenUrl(fact, collisionShift + 1L)

        fact.shortenedUrl = shortenedUrl
        return factRepository.saveFact(fact)
    }
}