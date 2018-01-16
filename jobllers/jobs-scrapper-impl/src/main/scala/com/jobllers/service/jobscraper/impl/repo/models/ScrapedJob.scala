package com.jobllers.service.jobscraper.impl.repo.models

import com.jobllers.common.util.date.{DateTimeReads, DateTimeWritesWithTAndSpecialChars}
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json.{Format, Json}

case class ScrapedJobListing(
                              jobId: String,
                              jobTitle: String,
                              company: String,
                              experience: String,
                              jobLocation: String,
                              popularityTag: String,
                              salaryOffered: String,
                              postedBy: String,
                              bannerOnListing: Boolean,
                              date: String,
                              jobURL: String
                            ) {
  def createScrapedJob(): ScrapedJob = {
    ScrapedJob(jobId, jobTitle, Some(company), Some(experience), jobLocation.trim.split(",").toList, Some(popularityTag),
      salaryOffered, Some(postedBy), Some(bannerOnListing), generateDate(date), jobURL)
  }

  private def generateDate(day: String): DateTime = {
    if (day.contains("ago")) {
      DateTime.now(DateTimeZone.forOffsetHoursMinutes(5, 30)).minusDays(1).withZone(DateTimeZone.UTC)
    } else {
      DateTime.now(DateTimeZone.forOffsetHoursMinutes(5, 30)).withZone(DateTimeZone.UTC)
    }
  }
}

/*
object ScrapedJobListing {
  implicit val format: Format[ScrapedJobListing] = Json.format
}
*/

case class ScrapedJob(
                       jobId: String,
                       jobTitle: String,
                       company: Option[String],
                       experience: Option[String],
                       jobLocations: List[String],
                       popularityTag: Option[String],
                       salaryOffered: String,
                       postedBy: Option[String],
                       bannerOnListing: Option[Boolean],
                       jobDate: DateTime,
                       jobUrl: String,
                       jobViews: Option[Int] = None,
                       jobApplicants: Option[Int] = None,
                       industry: List[String] = List.empty[String],
                       functionalArea: List[String] = List.empty[String],
                       roleCategory: Option[String] = None,
                       role: Option[String] = None,
                       employmentType: Option[String] = None,
                       keySkills: List[String] = List.empty[String],
                       jobDescription: Option[String] = None,
                       companyProfile: Option[String] = None,
                       desiredCandidateProfile: Option[String] = None
                     )

object ScrapedJob extends DateTimeReads with DateTimeWritesWithTAndSpecialChars {
  implicit val format: Format[ScrapedJob] = Json.format
}
