package com.jobllers.service.jobsscrapper.impl.repo

import akka.Done
import akka.actor.ActorSystem
import com.datastax.driver.core.{BoundStatement, PreparedStatement}
import com.jobllers.common.util.json.JsonHelper
import com.jobllers.service.jobsscrapper.impl.repo.cassandra.statement.CreateStatements
import com.jobllers.service.jobsscrapper.impl.repo.models.ScrapedJob
import com.jobllers.service.jobsscrapper.impl.util.{CassandraHelper, CassandraTable}
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}

class JobScrapperRepository(session: CassandraSession, actorSystem: ActorSystem)
                           (implicit ex: ExecutionContext) extends JsonHelper {

  var insertScrappedJobStmt: PreparedStatement = _

  def createTables(): Future[Done] = {
    CassandraHelper.createTable(session, CassandraTable(CreateStatements.createJobScrapperTable)).map(_ ⇒ akka.Done)
  }

  def createPreparedStmts(): Future[Done] = for {
    insertScrappedJob <- session.prepare("INSERT INTO scrapped_job_by_date_company_rolecategory_id JSON ?")
  } yield {
    insertScrappedJobStmt = insertScrappedJob
    Done
  }

  def insertScrappedJob(scrappedJob: ScrapedJob): Future[List[BoundStatement]] = {
    val jsonString: String = getJsonStringWithSnakeKeys(scrappedJob)
    Future.successful(List(insertScrappedJobStmt.bind(jsonString)))
  }

}
