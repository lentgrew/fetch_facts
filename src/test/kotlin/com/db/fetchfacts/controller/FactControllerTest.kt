package com.db.fetchfacts.controller

import com.db.fetchfacts.dto.CachedFact
import com.db.fetchfacts.dto.ShortenedFact
import com.db.fetchfacts.service.interfaces.FactService
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.core.Response
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`

@QuarkusTest
class FactControllerTest {

    @InjectMock
    private lateinit var factService: FactService

    @Test
    fun `when called fetchAndShortenFact then should return a shortened fact`() {
        val shortenedFact = ShortenedFact("fact", "url")

        `when`(factService.fetchAndShortenFact()).thenReturn(Uni.createFrom().item(shortenedFact))

        RestAssured.given()
                .contentType(ContentType.JSON)
                .post("/facts")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.statusCode)
                .body("original_fact", Matchers.equalTo("fact"))
                .body("shortened_url", Matchers.equalTo("url"))
    }

    @Test
    fun `when called fetchAndShortenFact then should return 500 on failure`() {
        `when`(factService.fetchAndShortenFact()).thenReturn(Uni.createFrom().failure(RuntimeException("Service failed")))

        RestAssured.given()
                .contentType(ContentType.JSON)
                .post("/facts")
                .then()
                .statusCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
                .body(Matchers.equalTo("Failed to fetch fact: Service failed"))
    }

    @Test
    fun `when called getFact then should return a cached fact`() {
        val cachedFact = CachedFact("fact", "link")
        `when`(factService.getFactByShortenedUrl("url")).thenReturn(cachedFact)

        RestAssured.given()
                .get("/facts/url")
                .then()
                .statusCode(Response.Status.OK.statusCode)
                .body("fact", Matchers.equalTo("fact"))
                .body("original_permalink", Matchers.equalTo("link"))
    }

    @Test
    fun `when called getAllFacts then should return all cached facts`() {
        val cachedFacts = listOf(
                CachedFact("fact1", "link1"),
                CachedFact("fact2", "link2")
        )
        `when`(factService.getAllFacts()).thenReturn(cachedFacts)

        RestAssured.given()
                .get("/facts")
                .then()
                .statusCode(Response.Status.OK.statusCode)
                .body("$.size()", Matchers.equalTo(2))
                .body("[0].fact", Matchers.equalTo("fact1"))
                .body("[0].original_permalink", Matchers.equalTo("link1"))
                .body("[1].fact", Matchers.equalTo("fact2"))
                .body("[1].original_permalink", Matchers.equalTo("link2"))
    }

    @Test
    fun `when called redirectToOriginal then should return a 302 redirect`() {
        `when`(factService.getOriginalPermalink("url")).thenReturn("http://test.com")

        RestAssured.given()
                .redirects().follow(false)
                .get("/facts/url/redirect")
                .then()
                .log().all()
                .statusCode(Response.Status.FOUND.statusCode)
                .header("Location", "http://test.com")
    }
}