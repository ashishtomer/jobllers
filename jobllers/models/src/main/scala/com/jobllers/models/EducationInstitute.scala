package com.jobllers.models

import play.api.libs.json.{Format, Json}

case class EducationInstitute(
                             name: String,
                             location: String,
                             `type`: String
                             )

object EducationInstitute {
  implicit val format: Format[EducationInstitute] = Json.format
}
