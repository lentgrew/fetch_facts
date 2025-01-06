package com.db.fetchfacts.controller

import com.db.fetchfacts.dto.StatisticResponse
import com.db.fetchfacts.service.interfaces.FactService
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.RestAssured
import jakarta.ws.rs.core.Response
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`

@QuarkusTest
class AdminControllerTest {

    @InjectMock
    private lateinit var factService: FactService

    @Test
    @TestSecurity(user = "admin", roles = ["admin"])
    fun `getAccessStatistics should return statistics when user has admin role`() {
        val statistics = listOf(
                StatisticResponse("url1", 10),
                StatisticResponse("url2", 20)
        )

        `when`(factService.getStatistics()).thenReturn(statistics)

        RestAssured.given()
                .`when`()
                .get("/admin/statistics")
                .then()
                .statusCode(Response.Status.OK.statusCode)
                .body("$.size()", Matchers.equalTo(2))
                .body("[0].shortened_url", Matchers.equalTo("url1"))
                .body("[0].access_count", Matchers.equalTo(10))
                .body("[1].shortened_url", Matchers.equalTo("url2"))
                .body("[1].access_count", Matchers.equalTo(20))
    }

    @Test
    @TestSecurity(user = "user", roles = ["user"])
    fun `when called getAccessStatistics then should return 403 when user does not have admin role`() {
        RestAssured.given()
                .`when`()
                .get("/admin/statistics")
                .then()
                .statusCode(Response.Status.FORBIDDEN.statusCode)
    }

    @Test
    fun `when called getAccessStatistics then should return 401 when no user is logged in`() {
        RestAssured.given()
                .`when`()
                .get("/admin/statistics")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.statusCode)
    }
}