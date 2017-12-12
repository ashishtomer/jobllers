package com.jobllers.service.joblisting.impl.elasticsearch

import com.jobllers.service.joblisting.impl.elasticsearch.Request.IndexedJob

import scala.concurrent.ExecutionContext

class ElasticSearchIndexedStore(es: ElasticSearch)(implicit ec: ExecutionContext) extends IndexedStore {

  override def storeDirectJob(jobDocument: IndexedJob) =
    es.putDoc("job", "direct", jobDocument.job.id.toString).invoke(jobDocument.job.getJob)


}
