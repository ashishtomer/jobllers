package com.jobllers.service.joblisting.impl

import com.jobllers.models.common.JobllersResponse
import com.jobllers.models.job.Job
import com.jobllers.service.joblisting.api.JobListingService
import com.jobllers.service.joblisting.impl.elasticsearch.{ESJobIndexer, ElasticSearchIndexedStore}
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.ExecutionContext

class JobListingServiceImpl(esis: ElasticSearchIndexedStore)(implicit ec: ExecutionContext) extends JobListingService
  with ESJobIndexer {

  override def addJob: ServiceCall[Job, JobllersResponse] = ServiceCall[Job, JobllersResponse] { job ⇒
    indexJobInES(esis, job)
  }

  override def updateJob(jobId: String): ServiceCall[Job, JobllersResponse] = ServiceCall[Job, JobllersResponse] { job ⇒
    updateJobInES(jobId, esis, job)
  }

}
