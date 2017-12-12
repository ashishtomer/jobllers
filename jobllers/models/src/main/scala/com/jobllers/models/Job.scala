package com.jobllers.models

import com.jobllers.models.JobTypeEnum.JobType
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}

case class Job(
                jobName: String,
                dateOfPosting: DateTime,
                dateOfJoining: Option[DateTime],
                jobType: JobType,
                location: String,
                company: String,
                keySkills: List[String],
                minExperience: Int,
                maxExperience: Option[Int],
                industry: Option[String],
                functionalArea: Option[String],
                roleCategory: Option[String],
                role: Option[String],
                minSalary: Option[Salary],
                maxSalary: Option[Salary],
                desiredCandidateProfile: Option[CandidateProfile],
                additionalInfo: Option[String]
              )

object Job {
  implicit val format: Format[Job] = Json.format
}
