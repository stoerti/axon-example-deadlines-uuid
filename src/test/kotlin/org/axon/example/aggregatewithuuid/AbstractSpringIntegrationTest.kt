package org.axon.example.aggregatewithuuid

import org.assertj.core.api.Assertions
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventsourcing.eventstore.EventStore
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*

@SpringBootTest
@Testcontainers
@ActiveProfiles("itest")
@Import(value = [ITestConfiguration::class])
class SpringIntegrationTest {

  @Autowired
  private lateinit var commandGateway: CommandGateway

  @Autowired
  private lateinit var eventStore: EventStore

  @Test
  fun testDeadlineCanceled() {
    // given aggregate with deadline is created
    val aggregateId = UUID.randomUUID()
    commandGateway.sendAndWait<Any>(CreateExampleCommand(aggregateId))

    // when I cancel the deadline
    commandGateway.sendAndWait<Any>(CancelDeadlineCommand(aggregateId))

    // the no event shall be fired after given time
    Thread.sleep(10000) // a bit longer so poll loop of quartz definitely runs
    Assertions.assertThat(eventStore.lastSequenceNumberFor(aggregateId.toString())).isEqualTo(2)
  }

}