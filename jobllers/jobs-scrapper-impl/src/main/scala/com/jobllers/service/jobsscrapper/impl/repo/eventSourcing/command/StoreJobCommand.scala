package com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.command

import akka.Done
import com.jobllers.common.models.job.Job
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

trait JobScrapperCommand

case class StoreJobCommand(job: Job) extends JobScrapperCommand with ReplyType[Done]

object StoreJobCommand {
  implicit val format: Format[StoreJobCommand] = Json.format
}
