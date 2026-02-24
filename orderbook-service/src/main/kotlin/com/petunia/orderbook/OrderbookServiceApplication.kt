package com.petunia.orderbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderbookServiceApplication

fun main(args: Array<String>) {
    runApplication<OrderbookServiceApplication>(*args)
}
