package com.jobllers.service.joblisting.api

import com.jobllers.common.models.common.JobllersResponse
import com.jobllers.common.models.job.Job
import com.jobllers.models.job.Job
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait JobListingService extends Service {

  def addJob: ServiceCall[Job, JobllersResponse]
  def updateJob(jobId: String): ServiceCall[Job, JobllersResponse]

  override def descriptor = {
    import Service._
    named("job_listing_service")
      .withCalls(
        restCall(Method.POST, "/add_job", addJob _),
        restCall(Method.PUT, "/update_job/:jobId", updateJob _)
      )
      .withAutoAcl(true)
  }

}
