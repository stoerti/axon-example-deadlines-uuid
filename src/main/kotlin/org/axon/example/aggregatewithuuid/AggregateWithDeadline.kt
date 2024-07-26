package org.axon.example.aggregatewithuuid

import mu.KLogging
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.deadline.DeadlineManager
import org.axonframework.deadline.annotation.DeadlineHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate
import java.time.Duration
import java.util.UUID

@Aggregate
class AggregateWithDeadline() {

  companion object : KLogging()

  @AggregateIdentifier
  private lateinit var id: UUID

  @CommandHandler
  @CreationPolicy(AggregateCreationPolicy.ALWAYS)
  fun create(cmd: CreateExampleCommand, deadlineManager: DeadlineManager) {
    logger.info { "Creating aggregate with id ${cmd.id} and scheduling deadline" }
    AggregateLifecycle.apply(AggregateCreatedEvent(cmd.id))

    deadlineManager.schedule(Duration.ofSeconds(1), "myDeadline")
  }

  @CommandHandler
  fun cancelDeadline(cmd: CancelDeadlineCommand, deadlineManager: DeadlineManager) {
    logger.info { "Canceling deadline for aggregate with id ${cmd.id}" }
    deadlineManager.cancelAllWithinScope("myDeadline")

    AggregateLifecycle.apply(DeadlineCanceledEvent(this.id))
  }

  @DeadlineHandler(deadlineName = "myDeadline")
  fun onMyDeadline() {
    logger.info { "Executing deadline for aggregate $id" }
    AggregateLifecycle.apply(DeadlineTriggeredEvent(this.id))
  }

  @EventSourcingHandler
  fun on(evt: AggregateCreatedEvent) {
    this.id = evt.id
  }
}