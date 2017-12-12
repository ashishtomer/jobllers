package com.jobllers.service.joblisting.api

import com.jobllers.models.Job
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait JobListingService extends Service {

  def addJob: ServiceCall[Job, String]

  override def descriptor = {
    import Service._
    named("job_listing_service")
      .withCalls(
        restCall(Method.POST, "/add_job", addJob _)
      )
      .withAutoAcl(true)
  }

}
