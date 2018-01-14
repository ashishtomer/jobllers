package com.jobllers.service.joblisting.impl.elasticsearch.request

import java.util.UUID

import com.jobllers.common.models.job.{CandidateProfile, Job, Salary}
import com.jobllers.models.job
import com.jobllers.common.models.job.JobTypeEnum.JobType
import com.jobllers.models.job.{CandidateProfile, Job, Salary}
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}

case class JobWithUUID(
                        id: UUID,
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
                      ) {
  def getJob: Job = Job(jobName, dateOfPosting, dateOfJoining, jobType, location, company, keySkills, minExperience,
    maxExperience, industry, functionalArea, roleCategory, role, minSalary, maxSalary, desiredCandidateProfile, additionalInfo)

}

object JobWithUUID {

  def apply(job: Job): JobWithUUID = JobWithUUID(UUID.randomUUID(), job.jobName, job.dateOfPosting,
    job.dateOfJoining, job.jobType, job.location, job.company, job.keySkills, job.minExperience, job.maxExperience,
    job.industry, job.functionalArea, job.roleCategory, job.role, job.minSalary, job.maxSalary,
    job.desiredCandidateProfile, job.additionalInfo)

  implicit val format: Format[JobWithUUID] = Json.format
}
