package org.axon.example.aggregatewithuuid

import java.util.UUID

data class AggregateCreatedEvent(
  val id: UUID
)

data class DeadlineCanceledEvent(
  val id: UUID
)

data class DeadlineTriggeredEvent(
  val id: UUID
)
