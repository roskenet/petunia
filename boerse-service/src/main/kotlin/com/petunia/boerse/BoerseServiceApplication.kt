package com.petunia.boerse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BoerseServiceApplication

fun main(args: Array<String>) {
    runApplication<BoerseServiceApplication>(*args)
}
