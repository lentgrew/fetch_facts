package com.db.fetchfacts.controller

import com.db.fetchfacts.dto.StatisticResponse
import com.db.fetchfacts.service.interfaces.FactService
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response

@Path("/admin/statistics")
class AdminController(
        private val factService: FactService
) {
    @GET
    fun getAccessStatistics():  List<StatisticResponse> {
        return factService.getStatistics()
    }
}