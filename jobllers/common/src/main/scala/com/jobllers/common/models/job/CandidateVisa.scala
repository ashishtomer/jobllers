package com.jobllers.common.models.job

import VisaStatusEnum.VisaStatus
import VisaTypeEnum.VisaType
import play.api.libs.json.{Format, Json}

case class CandidateVisa(
                        visaCountry: String,
                        visaStatus: VisaStatus,
                        visaType: VisaType
                        )

object CandidateVisa {
  implicit val format: Format[CandidateVisa] = Json.format
}
