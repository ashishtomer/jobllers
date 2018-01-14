package com.jobllers.service.joblisting.impl.elasticsearch

import akka.NotUsed
import com.jobllers.common.models.job.Job
import com.jobllers.service.joblisting.impl.elasticsearch.response.{ESDeleteResponse, ESIndexResponse}
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{CircuitBreaker, Descriptor, Service, ServiceCall}

trait ElasticSearch extends Service {

  override def descriptor: Descriptor = named("elastic-search")
    .withCalls(
      restCall(Method.PUT, "/:index/:type/:id/_create", putDoc _)
        .withCircuitBreaker(CircuitBreaker.identifiedBy("elasticsearch-circuitbreaker")),
      restCall(Method.PUT, "/:index/:type/:id/_update", updateDoc _)
        .withCircuitBreaker(CircuitBreaker.identifiedBy("elasticsearch-circuitbreaker")),
      restCall(Method.DELETE, "/:index/:type/:id/", deleteDoc _)
        .withCircuitBreaker(CircuitBreaker.identifiedBy("elasticsearch-circuitbreaker"))
    )

  def putDoc(index: String, `type`: String, itemId: String): ServiceCall[Job, ESIndexResponse]
  def updateDoc(index: String, `type`: String, itemId: String): ServiceCall[Job, ESIndexResponse]
  def deleteDoc(index: String, `type`: String, itemId: String): ServiceCall[NotUsed, ESDeleteResponse]
}
