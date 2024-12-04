package com.db.fetchfacts.controller

import com.db.fetchfacts.dto.StatisticResponse
import com.db.fetchfacts.service.interfaces.FactService
import jakarta.annotation.security.RolesAllowed
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

@Path("/admin/statistics")
class AdminController(
        private val factService: FactService
) {
    @GET
    @RolesAllowed("admin")
    fun getAccessStatistics():  List<StatisticResponse> {
        return factService.getStatistics()
    }
}