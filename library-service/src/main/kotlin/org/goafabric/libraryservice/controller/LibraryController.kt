package org.goafabric.libraryservice.controller

import jakarta.validation.Valid
import org.goafabric.libraryservice.controller.dto.Library
import org.goafabric.libraryservice.logic.LibraryLogic
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RequestMapping(value = ["/libraries"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@Validated
class LibraryController(private val libraryLogic: LibraryLogic) {
    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: String): Library {
        return libraryLogic.getById(id)
    }

    @GetMapping
    fun findAll(
        @RequestParam("page") page: Int, @RequestParam("size") size: Int
    ): List<Library> {
        return libraryLogic.findAll(page, size)
    }

    @GetMapping("search")
    fun findByName(
        @RequestParam("name") name: String,
        @RequestParam("page") page: Int, @RequestParam("size") size: Int
    ): List<Library> {
        return libraryLogic.findByName(name, page, size)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody library: @Valid Library): Library {
        return libraryLogic.save(library)
    }

    @DeleteMapping("{id}")
    fun deleteById(@PathVariable("id") id: String) {
        libraryLogic.deleteById(id)
    }
}
