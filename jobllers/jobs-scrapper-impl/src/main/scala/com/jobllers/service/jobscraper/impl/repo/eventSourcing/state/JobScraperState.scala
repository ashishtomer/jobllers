package com.jobllers.service.jobscraper.impl.repo.eventSourcing.state

import com.jobllers.service.jobscraper.impl.repo.models.ScrapedJob
import play.api.libs.json.{Format, Json}

case class JobScraperState(job: Option[ScrapedJob], status: JobScraperStatus.Status)

object JobScraperState {
  implicit val format: Format[JobScraperState] = Json.format

  def startingState: JobScraperState = JobScraperState(None, JobScraperStatus.StartingState)

  def jobStoredState(job: ScrapedJob): JobScraperState = JobScraperState(Some(job), JobScraperStatus.Stored)

  def jobUpdatedState(job: ScrapedJob): JobScraperState = JobScraperState(Some(job), JobScraperStatus.Updated)

  def jobDeletedState(job: ScrapedJob): JobScraperState = JobScraperState(Some(job), JobScraperStatus.Deleted)
}
