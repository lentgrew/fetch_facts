package com.db.fetchfacts.controller

import com.db.fetchfacts.dto.CachedFact
import com.db.fetchfacts.service.interfaces.FactService
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.Response

@Path("/facts")
class FactController(
        private val factService: FactService
) {
    @POST
    fun fetchAndShortenFact(): Uni<Response> {
        return factService.fetchAndShortenFact()
                .onItem().transform { fact ->
                    Response.ok(fact).build()
                }
                .onFailure().recoverWithItem { throwable ->
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Failed to fetch fact: ${throwable.message}")
                            .build()
                }
    }

    @GET
    @Path("/{shortenedUrl}")
    fun getFact(@PathParam("shortenedUrl") shortenedUrl: String): CachedFact {
        return factService.getFactByShortenedUrl(shortenedUrl)
    }

    @GET
    fun getAllFacts(): List<CachedFact> {
        return factService.getAllFacts()
    }

    @GET
    @Path("/{shortenedUrl}/redirect")
    fun redirectToOriginal(@PathParam("shortenedUrl") shortenedUrl: String): Response {
        val originalPermalink = factService.getOriginalPermalink(shortenedUrl)
        return Response.status(Response.Status.FOUND).header("Location", originalPermalink).build()
    }
}