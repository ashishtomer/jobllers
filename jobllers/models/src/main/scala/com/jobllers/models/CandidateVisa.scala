package com.jobllers.models

import com.jobllers.models.VisaStatusEnum.VisaStatus
import com.jobllers.models.VisaTypeEnum.VisaType
import play.api.libs.json.{Format, Json}

case class CandidateVisa(
                        visaCountry: String,
                        visaStatus: VisaStatus,
                        visaType: VisaType
                        )

object CandidateVisa {
  implicit val format: Format[CandidateVisa] = Json.format
}
