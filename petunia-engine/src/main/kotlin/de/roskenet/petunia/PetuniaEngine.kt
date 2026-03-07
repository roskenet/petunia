package de.roskenet.petunia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class PetuniaEngine

fun main(args: Array<String>) {
    runApplication<PetuniaEngine>(*args)
}
