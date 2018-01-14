package com.jobllers.common.models.common

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class SuccessJobCreationData(result: String, id: UUID)

object SuccessJobCreationData {
  implicit val format: Format[SuccessJobCreationData] = Json.format
}
