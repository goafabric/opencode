package org.goafabric.librarieservice.persistence

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.Library
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.controller.dto.StudentSearch
import org.goafabric.librarieservice.extensions.UserContext
import org.goafabric.librarieservice.logic.BookLogic
import org.goafabric.librarieservice.logic.LibraryLogic
import org.goafabric.librarieservice.logic.StudentLogic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Consumer

@ApplicationScoped
class DemoDataImporter(
     @param:ConfigProperty(name = "database.provisioning.goals") private val goals: String,
     @param:ConfigProperty(name = "multi-tenancy.tenants") private val tenants: String,
    private val studentLogic: StudentLogic,
    private val libraryLogic: LibraryLogic,
    private val bookLogic: BookLogic
) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    fun onStart(@Observes ev: StartupEvent) {
        run()
    }

    fun run() {
        if (goals.contains("-import-demo-data")) {
            log.info("Importing demo data ...")
            importDemoData()
            log.info("Demo data import done ...")
        }

        if (goals.contains("-terminate")) {
            log.info("Terminating app ...")
            Quarkus.asyncExit()
        }
    }

    private fun importDemoData() {
        listOf(*tenants.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()).forEach(
            Consumer { tenant: String ->
                UserContext.tenantId = tenant
                if (studentLogic.search(StudentSearch(null, null, null), 1, 10).isEmpty()) {
                    insertData()
                }
            })
        UserContext.tenantId = "0"
    }

    private fun insertData() {
         // a student can lend one or more books
        val homer = studentLogic.save(createStudent("Homer", "Simpson", "1A001001"))
        val bart = studentLogic.save(createStudent("Bart", "Simpson", "1A001002"))

        val books = listOf(
            createBook("The Simpsons", "Matt Groening", "978-1234567890"),
            createBook("Treehouse of Horror", "The Simpsons", "978-0785105771")
        )
        val central = libraryLogic.save(createLibrary("Central", "Springfield", books))

        // lend an already persisted book to an already persisted student
        bookLogic.lend(central.books.first().id!!, homer.id!!)
        bookLogic.lend(central.books.get(1).id!!, bart.id!!)
    }

    private fun createStudent(firstName: String, lastName: String, matriculationNumber: String): Student {
        return Student(null, null, firstName, lastName, matriculationNumber)
    }

    private fun createLibrary(name: String, city: String, books: List<Book>): Library {
        return Library(null, null, name, city, books)
    }

    private fun createBook(title: String, author: String, isbn: String): Book {
        return Book(null, null, isbn, title, author, null)
    }
}
