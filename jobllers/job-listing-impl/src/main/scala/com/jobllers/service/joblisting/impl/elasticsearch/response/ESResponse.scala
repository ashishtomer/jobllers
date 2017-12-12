package com.jobllers.service.joblisting.impl.elasticsearch.response

import play.api.libs.json.{Format, Json}

case class ESResponse(result: String) {
  override def toString: String = Json.prettyPrint(Json.toJson(this))
}

object ESResponse {
  implicit val format: Format[ESResponse] = Json.format
}
