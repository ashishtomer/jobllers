package com.jobllers.service.joblisting.impl.elasticsearch

import com.jobllers.models.Job
import com.jobllers.service.joblisting.impl.elasticsearch.response.ESResponse
import com.lightbend.lagom.scaladsl.api.{CircuitBreaker, Descriptor, Service, ServiceCall}
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.transport.Method

trait ElasticSearch extends Service {

  override def descriptor: Descriptor = {
    named("elastic-search")
      .withCalls(
        restCall(Method.PUT, "/:index/:type/:id/", putDoc _)
          .withCircuitBreaker(CircuitBreaker.identifiedBy("elasticsearch-circuitbreaker"))
      )
  }

  def putDoc(index: String, `type`: String, itemId: String): ServiceCall[Job, ESResponse]

}
