package com.jobllers.service.joblisting.impl.elasticsearch.Request

import com.jobllers.models.Job
import play.api.libs.json.{Format, Json}


case class IndexedJob(job: JobWithUUID)

object IndexedJob {

  def apply(job: Job): IndexedJob = IndexedJob(JobWithUUID(job))

  implicit val format: Format[IndexedJob] = Json.format

}
