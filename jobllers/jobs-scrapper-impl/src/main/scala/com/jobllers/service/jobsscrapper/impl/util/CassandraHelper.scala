package com.jobllers.service.jobsscrapper.impl.util

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}


case class CassandraTable(tableCmd: String, typeCmd: Seq[String] = Nil, indexCmd: Seq[String] = Nil)

object CassandraHelper {

  def createTable(session: CassandraSession, cassandraTable: CassandraTable)
                 (implicit executionContext: ExecutionContext): Future[Seq[Done]] =
    createTables(session, Seq(cassandraTable))


  def createTables(session: CassandraSession, cassandraTables: Seq[CassandraTable])
                  (implicit executionContext: ExecutionContext): Future[Seq[Done]] =
    session.underlying().flatMap { underlyingSession =>
      Future.sequence(for {
        cTable <- cassandraTables
      } yield {
        cTable.typeCmd.foreach { statement =>
          underlyingSession.execute(statement)
        }
        session.executeCreateTable(cTable.tableCmd).map { _ =>
          cTable.indexCmd.foreach { statement =>
            underlyingSession.execute(statement)
          }
          Done
        }

      })
    }

}
