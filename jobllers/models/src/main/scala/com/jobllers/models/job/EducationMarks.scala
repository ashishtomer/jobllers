package com.jobllers.models.job

import com.jobllers.models.job.MarksTypeEnum.MarksType
import play.api.libs.json.{Format, Json}

case class EducationMarks(
                           marksType: MarksType,
                           amount: Double
                         ) {
  override def toString: String = amount + " " + marksType.toString
}

object EducationMarks {
  implicit val format: Format[EducationMarks] = Json.format
}
