package com.jobllers.service.joblisting.impl.elasticsearch

import com.jobllers.service.joblisting.impl.elasticsearch.Request.IndexedJob
import com.jobllers.service.joblisting.impl.elasticsearch.response.ESResponse

import scala.concurrent.Future

trait IndexedStore {

  def storeDirectJob(jobDocument: IndexedJob): Future[ESResponse]

}
