package com.jobllers.service.jobscraper.impl.repo.eventSourcing.event

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventShards, AggregateEventTag, AggregateEventTagger}

trait JobScraperEvent extends AggregateEvent[JobScraperEvent] {
  override def aggregateTag: AggregateEventTagger[JobScraperEvent] = JobScraperEvent.Tag
}

object JobScraperEvent {
  val Tag: AggregateEventShards[JobScraperEvent] = AggregateEventTag.sharded[JobScraperEvent](3)
}
