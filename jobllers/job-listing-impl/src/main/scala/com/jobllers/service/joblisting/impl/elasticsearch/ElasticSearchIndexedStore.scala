package com.jobllers.service.joblisting.impl.elasticsearch

import com.jobllers.service.joblisting.impl.elasticsearch.request.IndexedJob
import com.jobllers.service.joblisting.impl.elasticsearch.response.{ESDeleteResponse, ESIndexResponse}

import scala.concurrent.{ExecutionContext, Future}

class ElasticSearchIndexedStore(es: ElasticSearch)(implicit ec: ExecutionContext) extends IndexedStore {

  override def storeDirectJob(jobDocument: IndexedJob) = es.putDoc("job", "direct", jobDocument.job.id.toString)
    .invoke(jobDocument.job.getJob)

  override def updateDirectJob(jobId: String, jonDocument: IndexedJob): Future[ESIndexResponse] =
    es.updateDoc("job", "direct", jobId).invoke(jonDocument.job.getJob)

  override def deleteDirectJob(jobId: String): Future[ESDeleteResponse] =
    es.deleteDoc("job","direct", jobId).invoke()

}
