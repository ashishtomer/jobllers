package com.jobllers.service.joblisting.impl.elasticsearch.response

import play.api.libs.json.{Format, Json}

case class ESDeleteResponse(
                             found: Boolean,
                             _index: String,
                             _type: String,
                             _id: String,
                             _version: String
                           )

object ESDeleteResponse {
  implicit val format: Format[ESDeleteResponse] = Json.format
}
