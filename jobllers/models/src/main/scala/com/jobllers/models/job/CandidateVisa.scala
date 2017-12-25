package com.jobllers.models.job

import com.jobllers.models.job.VisaStatusEnum.VisaStatus
import com.jobllers.models.job.VisaTypeEnum.VisaType
import play.api.libs.json.{Format, Json}

case class CandidateVisa(
                        visaCountry: String,
                        visaStatus: VisaStatus,
                        visaType: VisaType
                        )

object CandidateVisa {
  implicit val format: Format[CandidateVisa] = Json.format
}
