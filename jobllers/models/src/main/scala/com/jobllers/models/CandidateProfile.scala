package com.jobllers.models

import play.api.libs.json.{Format, Json}

case class CandidateProfile(
                             education: List[Education],
                             origin: String,
                             spokenLanguage: List[String],
                             writtenLanguage: List[String],
                             workVisa: List[CandidateVisa],
                             details: Option[String]
                           )

object CandidateProfile {
  implicit val format: Format[CandidateProfile] = Json.format
}
