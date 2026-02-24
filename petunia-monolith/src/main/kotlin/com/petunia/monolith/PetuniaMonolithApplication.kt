package com.petunia.monolith

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetuniaMonolithApplication

fun main(args: Array<String>) {
    runApplication<PetuniaMonolithApplication>(*args)
}
