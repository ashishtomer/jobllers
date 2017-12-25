package com.jobllers.service.joblisting.impl.elasticsearch.response

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class ShardResponse(total: Int, successful: Int, failed: Int)

case class ErrorRootCause(
                           `type`: String,
                           reason: String,
                           shard: String,
                           index: String)

case class ESError(
                    rootCause: List[ErrorRootCause] = List.empty[ErrorRootCause],
                    `type`: String,
                    reason: String,
                    shard: String,
                    index: String
                  )

case class ESIndexResponse(
                       result: String,
                       created: Option[Boolean],
                       _index: String,
                       _type: String,
                       _id: UUID,
                       _version: Int,
                       _shards: Option[ShardResponse],
                       error: Option[ESError],
                       status: Option[Int]
                     )

object ESIndexResponse {
  implicit val format: Format[ESIndexResponse] = Json.format
}

object ShardResponse {
  implicit val format: Format[ShardResponse] = Json.format
}

object ErrorRootCause {
  implicit val format: Format[ErrorRootCause] = Json.format
}

object ESError {
  implicit val format: Format[ESError] = Json.format
}
