package com.jobllers.service.joblisting.impl

import com.jobllers.models.Job
import com.jobllers.service.joblisting.api.JobListingService
import com.jobllers.service.joblisting.impl.elasticsearch.ElasticSearchIndexedStore
import com.jobllers.service.joblisting.impl.elasticsearch.Request.IndexedJob
import com.lightbend.lagom.scaladsl.api.ServiceCall
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

class JobListingServiceImpl(esis: ElasticSearchIndexedStore)(implicit ec: ExecutionContext) extends JobListingService {

  def addJob: ServiceCall[Job, String] = ServiceCall[Job, String] { jobData ⇒

    esis.storeDirectJob(IndexedJob(jobData))
      .map { doneResponse ⇒
        Json.prettyPrint(Json.toJson(jobData))
      }
  }
}
