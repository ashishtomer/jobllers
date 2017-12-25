package com.jobllers.service.joblisting.impl.elasticsearch

import com.jobllers.models.common.{JobllersResponse, SuccessData, SuccessJobCreationData}
import com.jobllers.models.job.Job
import com.jobllers.service.joblisting.impl.elasticsearch.request.IndexedJob

import scala.concurrent.{ExecutionContext, Future}

trait ESJobIndexer {

  def indexJobInES(esis: ElasticSearchIndexedStore, job: Job)(implicit ec: ExecutionContext): Future[JobllersResponse] =
    esis.storeDirectJob(IndexedJob(job)).map(response ⇒ response.created match {
      case Some(true) ⇒ JobllersResponse(200, Some(SuccessData(message = "The job created successfully",
        Some(SuccessJobCreationData(response.result, response._id)))), None)
      case Some(false) ⇒ JobllersResponse(401, Some(SuccessData(message = "The job couldn't be creted",
        Some(SuccessJobCreationData(response.result, response._id)))), None)
      case _ ⇒ JobllersResponse(500, None, Some(List("Internal server error while creating job")))
    })

  def updateJobInES(jobId: String, esis: ElasticSearchIndexedStore, job: Job)(implicit ec: ExecutionContext)
  : Future[JobllersResponse] = esis.updateDirectJob(jobId, IndexedJob(job)).map(response ⇒ response.created match {
    case Some(false) ⇒ JobllersResponse(200, Some(SuccessData(message = "The job update successfully",
      Some(SuccessJobCreationData(response.result, response._id)))), None)
    case Some(true) ⇒ JobllersResponse(200, Some(SuccessData(message = "The job couldn't be updated",
      Some(SuccessJobCreationData(response.result, response._id)))), None)
    case _ ⇒ JobllersResponse(500, None, Some(List("Internal server error while creating job")))
  })

}
