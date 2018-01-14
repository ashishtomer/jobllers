package com.jobllers.common.models.job

import EducationLevelEnum.EducationLevel
import play.api.libs.json.{Format, Json}

case class Education(
                    educationLevel: EducationLevel,
                    course: String,
                    institute: EducationInstitute,
                    year: Int,
                    marks: EducationMarks,
                    awards: List[String]
                    )

object Education {
  implicit val format: Format[Education] = Json.format
}
