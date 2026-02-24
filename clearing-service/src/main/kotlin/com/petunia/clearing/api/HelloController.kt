package com.petunia.clearing.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/api/hello")
    fun hello(): Map<String, String> = mapOf("service" to "clearing", "message" to "Clearing Service is up")
}
