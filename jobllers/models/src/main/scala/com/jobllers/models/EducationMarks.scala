package com.jobllers.models

import com.jobllers.models.MarksTypeEnum.MarksType
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
