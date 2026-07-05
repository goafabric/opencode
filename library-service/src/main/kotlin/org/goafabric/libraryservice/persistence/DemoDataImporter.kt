package org.goafabric.libraryservice.persistence

import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import jakarta.validation.constraints.NotNull
import org.goafabric.libraryservice.persistence.entity.BookEo
import org.goafabric.libraryservice.persistence.entity.StudentEo

/**
 * Imports demo data on application startup.
 */
@ApplicationScoped
class DemoDataImporter {
     @Inject
      lateinit var studentRepo: StudentRepository
       @Inject
      lateinit var bookRepo: BookRepository

     /**
      * Called at startup to seed the database.
      */
    fun importData(@Observes event: StartupEvent) {
          // Seed Demo Students
        if (studentRepo.count() == 0L) {
            studentRepo.persist(StudentEo(name = "Alice"))
            studentRepo.persist(StudentEo(name = "Bob"))
         }

          // Seed Demo Books
         if (bookRepo.count() == 0L) {
             bookRepo.persist(BookEo(title = "The Quarkus Guide", isbn = "978-1234567890"))
             bookRepo.persist(BookEo(title = "Kotlin in Action", isbn = "978-0987654321"))
         }
     }
}
