package com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.state

import com.jobllers.common.models.job.Job
import play.api.libs.json.{Format, Json}

case class JobScrapperState(job: Option[Job], status: JobScrapperStatus.Status)

object JobScrapperState {
  implicit val format: Format[JobScrapperState] = Json.format

  def startingState: JobScrapperState = JobScrapperState(None, JobScrapperStatus.StartingState)
  def jobStoredState(job: Job): JobScrapperState = JobScrapperState(Some(job), JobScrapperStatus.Stored)
  def jobUpdatedState(job: Job): JobScrapperState = JobScrapperState(Some(job), JobScrapperStatus.Updated)
  def jobDeletedState(job: Job): JobScrapperState = JobScrapperState(Some(job), JobScrapperStatus.Deleted)
}
