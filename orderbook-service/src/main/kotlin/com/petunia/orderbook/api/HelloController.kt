package com.petunia.orderbook.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/api/hello")
    fun hello(): Map<String, String> = mapOf("service" to "orderbook", "message" to "Orderbook Service is up")
}
