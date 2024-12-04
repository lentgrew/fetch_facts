package com.db.fetchfacts.service

import com.db.fetchfacts.model.Fact
import com.db.fetchfacts.service.interfaces.ShortenedUrlService
import com.db.fetchfacts.utils.Base62
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ShortenedUrlServiceImpl : ShortenedUrlService {
    override fun shortenUrl(fact: Fact, collisionShift: Long): String {
        val hashCode = Base62.stringToHashCode(fact.id) + collisionShift
        val encodedUrl = Base62.encode(hashCode)
        return encodedUrl
    }
}