package org.axon.example.aggregatewithuuid

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

data class CreateExampleCommand(
  @TargetAggregateIdentifier
  val id: UUID
)

data class CancelDeadlineCommand(
  @TargetAggregateIdentifier
  val id: UUID
)
