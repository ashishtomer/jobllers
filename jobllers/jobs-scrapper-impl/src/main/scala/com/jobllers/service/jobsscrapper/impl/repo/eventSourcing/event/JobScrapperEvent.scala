package com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.event

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventShards, AggregateEventTag, AggregateEventTagger}

trait JobScrapperEvent extends AggregateEvent[JobScrapperEvent] {
  override def aggregateTag: AggregateEventTagger[JobScrapperEvent] = JobScrapperEvent.Tag
}

object JobScrapperEvent {
  val Tag: AggregateEventShards[JobScrapperEvent] = AggregateEventTag.sharded[JobScrapperEvent](3)
}
