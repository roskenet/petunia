package com.petunia.monolith.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/api/hello")
    fun hello(): Map<String, String> = mapOf("message" to "Petunia Monolith is up")
}
