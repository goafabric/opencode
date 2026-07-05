package org.goafabric.libraryservice

import io.quarkus.runtime.Quarkus

/**
 * Entry point for the Library Service.
 */
class Application {
    fun main() {
        Quarkus.run(*emptyArray())
    }
}
