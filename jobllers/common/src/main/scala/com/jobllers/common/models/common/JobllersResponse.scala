package com.jobllers.common.models.common

import play.api.libs.json.{Format, Json}

case class SuccessData(message: String,
                       data: Option[SuccessJobCreationData])

case class JobllersResponse(statusCode: Int,
                            success: Option[SuccessData],
                            errors: Option[List[String]])

object SuccessData {
  implicit val format: Format[SuccessData] = Json.format
}

object JobllersResponse {
  implicit val format: Format[JobllersResponse] = Json.format
}
