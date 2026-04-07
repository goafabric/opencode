package org.goafabric.librarieservice.persistence

import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.controller.dto.StudentSearch
import org.goafabric.librarieservice.extensions.UserContext
import org.goafabric.librarieservice.logic.BookLogic
import org.goafabric.librarieservice.logic.StudentLogic
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.Arrays
import java.util.function.Consumer
import java.util.stream.IntStream

@Component
class DemoDataImporter(
       @param:Value("\${database.provisioning.goals:}") private val goals: String,
       @param:Value("\${multi-tenancy.tenants}") private val tenants: String,
    private val applicationContext: ApplicationContext
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun run(vararg args: String) {
        if (args.size > 0 && "-check-integrity" == args[0]) {
            return
           }
        if (goals.contains("-import-demo-data")) {
            log.info("Importing library demo data ...")
            importDemoData()
            log.info("Library demo data import done ...")
           }
        if (goals.contains("-terminate")) {
            log.info("Terminating app ...")
            SpringApplication.exit(
                applicationContext,
                ExitCodeGenerator { 0 }) //if an exception is raised, spring will automatically terminate with 1
           }
       }

    private fun importDemoData() {
        Arrays.asList(*tenants.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()).forEach(
            Consumer { tenant: String? ->
                UserContext.tenantId = tenant!!;
                   // Check if we need to import data
                val studentLogic = applicationContext.getBean(StudentLogic::class.java)
                val bookLogic = applicationContext.getBean(BookLogic::class.java)
                    
                // Only insert if no students exist
               try {
                   if (studentLogic.find(StudentSearch(), 0, 1).isEmpty()) {
                       insertData(bookLogic)
                      }
                   } catch (e: Exception) {
                    log.warn("Skipping demo data for tenant: {}", e.message)
                   }
               })
        UserContext.tenantId = "0";
       }

    private fun insertData(bookLogic: BookLogic) {
        val studentLogic: StudentLogic = applicationContext.getBean(StudentLogic::class.java)
        
          // Create demo books first so they exist for loans
        createDemoBooks(bookLogic)
        
          // Create demo students
        IntStream.range(0, 3).forEach { i: Int ->
            val studentFirstName = listOf("Homer", "Bart", "Lisa")[i]
                val studentLastName = listOf("Simpson", "Smith", "Johnson")[i % 3]
                
            val savedStudent = studentLogic.save(
                Student(
                    id = null, version = null, matriculationNumber = "STU${10 + i}", 
                    firstName = studentFirstName, lastName = studentLastName
                  )
                )
            log.info("Created student: ${savedStudent.firstName} ${savedStudent.lastName}")
             }

        IntStream.range(3, 5).forEach { i: Int ->
            val studentFirstName = listOf("Albert", "Isaac")[i - 3]
                val studentLastName = listOf("Einstein", "Newton")[i - 3]
                
           try {
               val savedStudent = studentLogic.save(
                    Student(
                        id = null, version = null, matriculationNumber = "STU${10 + i}", 
                        firstName = studentFirstName, lastName = studentLastName
                      )
                  )
                log.info("Created student: ${savedStudent.firstName} ${savedStudent.lastName}")
               } catch (e: Exception) {
                log.warn("Could not create student: {}", e.message)
               }
              }
       
        log.info("Demo data import completed for tenant: ${UserContext.tenantId}")
       }

    private fun createDemoBooks(bookLogic: BookLogic) {
        val books = listOf(
            Book(id = null, version = null, isbn = "978-0-13-468599-1", title = "The Pragmatic Programmer", author = "David Thomas", publisher = "Addison-Wesley", publicationYear = 2019),
            Book(id = null, version = null, isbn = "978-0-201-63361-0", title = "Design Patterns", author = "Gang of Four", publisher = "Addison-Wesley", publicationYear = 1994),
            Book(id = null, version = null, isbn = "978-0-596-51774-8", title = "JavaScript: The Good Parts", author = "Douglas Crockford", publisher = "O'Reilly", publicationYear = 2008),
            Book(id = null, version = null, isbn = "978-0-321-20007-3", title = "Clean Code", author = "Robert C. Martin", publisher = "Prentice Hall", publicationYear = 2008),
            Book(id = null, version = null, isbn = "978-0-13-235088-4", title = "Domain Driven Design", author = "Eric Evans", publisher = "Addison-Wesley", publicationYear = 2003),
            Book(id = null, version = null, isbn = "978-0-596-00712-6", title = "Head First Design Patterns", author = "Kathy Sierra", publisher = "O'Reilly", publicationYear = 2004),
            Book(id = null, version = null, isbn = "978-1-491-95035-7", title = "Spring Boot in Action", author = "Craig Walls", publisher = "Manning", publicationYear = 2021)
           )

        books.forEach { bookTemplate ->
            try {
                val savedBook = bookLogic.save(bookTemplate)
                log.info("Created book: ${savedBook.title} by ${savedBook.author}")
                } catch (e: Exception) {
                   if (!e.message?.contains("already exists") == true) {
                       log.warn("Could not create book ${bookTemplate.title}: {}", e.message)
                      }
               }
            }
        }

// Flyway migration files will handle database schema setup in db/migration folder
}
