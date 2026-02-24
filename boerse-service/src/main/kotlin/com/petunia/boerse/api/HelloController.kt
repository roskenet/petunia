package com.petunia.boerse.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/api/hello")
    fun hello(): Map<String, String> = mapOf("service" to "boerse", "message" to "Boerse Service is up")
}
