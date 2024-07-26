package org.axon.example.aggregatewithuuid

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AxonExampleDeadlinesUuidApplication

fun main(args: Array<String>) {
  runApplication<AxonExampleDeadlinesUuidApplication>(*args)
}
