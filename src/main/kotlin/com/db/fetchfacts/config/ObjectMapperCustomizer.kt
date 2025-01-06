package com.db.fetchfacts.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ObjectMapperCustomizer : io.quarkus.jackson.ObjectMapperCustomizer {
    override fun customize(objectMapper: ObjectMapper) {
        objectMapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    }
}