package com.db.fetchfacts.client

import com.db.fetchfacts.model.Fact
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.GET
import jakarta.ws.rs.QueryParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "facts-api")
interface FactsClient {
    @GET
    fun getRandomFact(@QueryParam("language") language: String): Uni<Fact>
}