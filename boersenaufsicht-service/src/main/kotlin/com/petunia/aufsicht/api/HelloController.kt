package com.petunia.aufsicht.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/api/hello")
    fun hello(): Map<String, String> = mapOf("service" to "boersenaufsicht", "message" to "Boersenaufsicht Service is up")
}
