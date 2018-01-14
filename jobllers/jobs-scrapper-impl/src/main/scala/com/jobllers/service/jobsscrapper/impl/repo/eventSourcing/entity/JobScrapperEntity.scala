package com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.entity

import com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.command.JobScrapperCommand
import com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.event.JobScrapperEvent
import com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.state.JobScrapperState
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class JobScrapperEntity extends PersistentEntity {
  override type Command = JobScrapperCommand
  override type Event = JobScrapperEvent
  override type State = JobScrapperState

  override def initialState = ???

  override def behavior = ???
}
