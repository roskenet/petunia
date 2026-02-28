package de.roskenet.petunia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetuniaEngine

fun main(args: Array<String>) {
    runApplication<PetuniaEngine>(*args)
}
