package com.projectpetunia.centralbank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CentralBankApplication

fun main(args: Array<String>) {
    runApplication<CentralBankApplication>(*args)
}
