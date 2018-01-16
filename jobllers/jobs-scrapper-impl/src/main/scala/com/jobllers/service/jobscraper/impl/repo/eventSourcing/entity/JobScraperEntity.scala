package com.jobllers.service.jobscraper.impl.repo.eventSourcing.entity

import akka.Done
import com.jobllers.service.jobscraper.impl.repo.eventSourcing.command.{JobScraperCommand, StoreScrapedJobCommand}
import com.jobllers.service.jobscraper.impl.repo.eventSourcing.event.{JobScraperEvent, ScrapedJobStoredEvent}
import com.jobllers.service.jobscraper.impl.repo.eventSourcing.state.{JobScraperState, JobScraperStatus}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class JobScraperEntity extends PersistentEntity {
  override type Command = JobScraperCommand
  override type Event = JobScraperEvent
  override type State = JobScraperState

  private val jobScraperAction: Actions =
    Actions()
      .onCommand[StoreScrapedJobCommand, Done] {
      case (StoreScrapedJobCommand(scrapedJob), ctx, _) ⇒
        ctx.thenPersist(ScrapedJobStoredEvent(scrapedJob)){x ⇒ ctx.reply(Done)}
    }
    .onEvent {
      case (ScrapedJobStoredEvent(scrapedJob), state) ⇒
        state.copy(job = Some(scrapedJob), status = JobScraperStatus.Stored)
    }

  override def initialState = JobScraperState.startingState

  override def behavior = {
    case JobScraperState(_, _) ⇒ jobScraperAction
  }
}
