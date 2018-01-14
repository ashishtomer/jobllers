package com.jobllers.service.joblisting.impl.elasticsearch.request

import com.jobllers.common.models.job.Job
import play.api.libs.json.{Format, Json}


case class IndexedJob(job: JobWithUUID)

object IndexedJob {

  def apply(job: Job): IndexedJob = IndexedJob(JobWithUUID(job))

  implicit val format: Format[IndexedJob] = Json.format

}
