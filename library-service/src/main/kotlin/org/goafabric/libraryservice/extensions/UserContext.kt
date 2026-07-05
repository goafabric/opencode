package org.goafabric.libraryservice.extensions

import jakarta.enterprise.context.ApplicationScoped

/**
 * Represents the currently authenticated user context.
 */
@ApplicationScoped
class UserContext {
    var username: String = "anonymous"
}
