package org.goafabric.libraryservice.persistence

import org.goafabric.libraryservice.controller.dto.*
import org.goafabric.libraryservice.extensions.UserContext
import org.goafabric.libraryservice.logic.BookLendingLogic
import org.goafabric.libraryservice.logic.BookLogic
import org.goafabric.libraryservice.logic.LibraryLogic
import org.goafabric.libraryservice.logic.StudentLogic
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.Arrays
import java.util.function.Consumer

@Component
class DemoDataImporter(
    @param:Value("\${database.provisioning.goals:}") private val goals: String, @param:Value(
        "\${multi-tenancy.tenants}"
    ) private val tenants: String,
    private val applicationContext: ApplicationContext
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this.javaClass)
    override fun run(vararg args: String) {
        if (args.isNotEmpty() && "-check-integrity" == args[0]) {
            return
        }
        if (goals.contains("-import-demo-data")) {
            log.info("Importing demo data ...")
            importDemoData()
            log.info("Demo data import done ...")
        }
        if (goals.contains("-terminate")) {
            log.info("Terminating app ...")
            SpringApplication.exit(
                applicationContext,
                ExitCodeGenerator { 0 })
        }
    }

    private fun importDemoData() {
        Arrays.asList(*tenants.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()).forEach(
            Consumer { tenant: String? ->
                UserContext.tenantId = tenant!!;
                if (applicationContext.getBean(LibraryLogic::class.java).findAll(0, 1).isEmpty()) {
                    insertData()
                }
            })
        UserContext.tenantId = "0";
    }

    private fun insertData() {
        val libraryLogic = applicationContext.getBean(LibraryLogic::class.java)
        val bookLogic = applicationContext.getBean(BookLogic::class.java)
        val studentLogic = applicationContext.getBean(StudentLogic::class.java)
        val bookLendingLogic = applicationContext.getBean(BookLendingLogic::class.java)

        // Create libraries
        val centralLibrary = libraryLogic.save(
            Library(name = "Central City Library", address = "100 Main Street, Springfield " + UserContext.tenantId)
        )
        val universityLibrary = libraryLogic.save(
            Library(name = "University Library", address = "42 Campus Drive, Springfield " + UserContext.tenantId)
        )

        // Create books for Central City Library
        val book1 = bookLogic.save(
            Book(title = "Clean Code", author = "Robert C. Martin", isbn = "9780132350884", libraryId = centralLibrary.id!!)
        )
        val book2 = bookLogic.save(
            Book(title = "Design Patterns", author = "Gang of Four", isbn = "9780201633610", libraryId = centralLibrary.id!!)
        )
        val book3 = bookLogic.save(
            Book(title = "The Pragmatic Programmer", author = "David Thomas", isbn = "9780135957059", libraryId = centralLibrary.id!!)
        )

        // Create books for University Library
        val book4 = bookLogic.save(
            Book(title = "Introduction to Algorithms", author = "Thomas H. Cormen", isbn = "9780262033848", libraryId = universityLibrary.id!!)
        )
        val book5 = bookLogic.save(
            Book(title = "Structure and Interpretation", author = "Harold Abelson", isbn = "9780262510875", libraryId = universityLibrary.id!!)
        )

        // Create students
        val student1 = studentLogic.save(
            Student(firstName = "Homer", lastName = "Simpson", email = "homer.simpson@springfield.edu")
        )
        val student2 = studentLogic.save(
            Student(firstName = "Bart", lastName = "Simpson", email = "bart.simpson@springfield.edu")
        )
        val student3 = studentLogic.save(
            Student(firstName = "Lisa", lastName = "Simpson", email = "lisa.simpson@springfield.edu")
        )

        // Create some book lendings
        bookLendingLogic.lendBook(student1.id!!, book1.id!!, LocalDate.now().plusDays(14))
        bookLendingLogic.lendBook(student2.id!!, book2.id!!, LocalDate.now().plusDays(14))
        bookLendingLogic.lendBook(student3.id!!, book4.id!!, LocalDate.now().plusDays(21))
        bookLendingLogic.lendBook(student3.id!!, book5.id!!, LocalDate.now().plusDays(21))
    }
}
