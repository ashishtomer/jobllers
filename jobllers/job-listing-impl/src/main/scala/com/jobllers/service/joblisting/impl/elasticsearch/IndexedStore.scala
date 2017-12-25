package com.jobllers.service.joblisting.impl.elasticsearch

import com.jobllers.service.joblisting.impl.elasticsearch.request.IndexedJob
import com.jobllers.service.joblisting.impl.elasticsearch.response.{ESDeleteResponse, ESIndexResponse}

import scala.concurrent.Future

trait IndexedStore {

  def storeDirectJob(jobDocument: IndexedJob): Future[ESIndexResponse]
  def updateDirectJob(jobId: String, jonDocument: IndexedJob): Future[ESIndexResponse]
  def deleteDirectJob(jobId: String): Future[ESDeleteResponse]
}
