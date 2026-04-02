package org.goafabric.libraryservice.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.goafabric.libraryservice.persistence.entity.BookEo

interface BookRepository : JpaRepository<BookEo, Long>