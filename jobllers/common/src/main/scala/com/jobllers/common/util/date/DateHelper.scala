package com.jobllers.common.util.date

import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json._

/**
  * A note to remind to self how the formats and dates work:
  * 1. A DateTime object is always in the form - <b>2017-11-20T11:43:08.191Z</b>
  * 2. Conversion from one from format to another doesn't make any changes in the representation of the DateTime object
  * itself.
  * 3. The conversion from one format to another is just asking the DateTime object to represent its date in a
  * particular pattern. For example :
  * val dateTime = DateTime.now().withZone(DateTimeZone.UTC) <-> dateTime: org.joda.time.DateTime = 2017-11-20T11:43:08.191Z
  * val x: String = dateTime.toString(dateTimePatternWithTForClient) <-> x: String = 20171120T114308.191+0000
  *
  * 4. If there is a date in String, then it can be parsed with the only pattern that represents that String og that date.
  * Example:
  * val dateTimeFormatWithTForClient = DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss.SSSZ")
  * DateTime.parse("20171212T215959.000Z", dateTimeFormatWithTForClient) <-> res0: org.joda.time.DateTime = 2017-12-12T21:59:59.000Z
  *
  */

object DateHelper {

  val dateTimePatternWithT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
  val dateTimePatternWithSpace = "yyyy-MM-dd HH:mm:ss.SSSZ"
  val dateTimePatternWithTForClient = "yyyyMMdd'T'HHmmss.SSSZ"

  val dateTimeFormatWithT = DateTimeFormat.forPattern(dateTimePatternWithT).withZoneUTC()
  val dateTimeFormatWithSpace = DateTimeFormat.forPattern(dateTimePatternWithSpace).withZoneUTC()
  val dateTimeFormatWithTForClient: DateTimeFormatter = DateTimeFormat.forPattern(dateTimePatternWithTForClient).withZoneUTC()

  def nowUTC(): DateTime = DateTime.now().withZone(DateTimeZone.UTC)

  //Returns the date's month. Example - november, october
  def monthOfDateTime(dateTime: DateTime): String = dateTime.toString("MMMM").toLowerCase()

  def yesterdayBeginning(): DateTime = {
    val yesterday00 = DateTime.now().toString("yyyyMMdd'T'") + "000000.000Z"
    dateTimeFormatWithTForClient.parseDateTime(yesterday00).minusDays(1).withZone(DateTimeZone.UTC)
  }

  def yesterdayEnd(): DateTime = {
    val yesterday24 = DateTime.now().toString("yyyyMMdd'T'") + "235959.999Z"
    dateTimeFormatWithTForClient.parseDateTime(yesterday24).minusDays(1).withZone(DateTimeZone.UTC)
  }

  def formatDateForEndUser(dateTime: DateTime) : String = {
    dateTime.toString(dateTimeFormatWithTForClient)
  }

  def formatDateWithTAndSpecialChars(dateTime: DateTime) : String = {
    dateTime.toString(dateTimeFormatWithT)
  }


  def parseDateTimeString(date: String): DateTime = {

    val dateString: String = completeDateTimeString(date.trim)

    if(isDateFormatForClient(dateString)) {
      DateTime.parse(dateString, dateTimeFormatWithTForClient)
    }
    else if(isDateWithTAndSpecialChars(dateString)) {
      DateTime.parse(dateString, dateTimeFormatWithT)
    }
    else if(isDateWithSpaceAndSpecialChars(dateString)) {
      DateTime.parse(dateString, dateTimeFormatWithSpace)
    }
    else if (isDateWithSpecialCharsWithoutTime(dateString)) {
      DateTime.parse(dateString, dateTimeFormatWithT)
    }
    //If dateString is in format yyyyMMdd (only date)
    else if (isDateForClientWithoutTime(dateString)) {
      DateTime.parse(dateString, dateTimeFormatWithTForClient)
    }
    else {
      DateTime.parse(dateString)
    }
  }

  //scalastyle:off
  def completeDateTimeString(date: String): String = {

    val dateString: String = if(!(isDateWithTAndSpecialChars(date) || isDateWithSpaceAndSpecialChars(date)) && date.length > 19) {
      date.substring(0, 19) + "Z"
    } else if((isDateWithTAndSpecialChars(date) || isDateWithSpaceAndSpecialChars(date)) && date.length > 23) {
      date.substring(0, 23) + "Z"
    } else {
      date
    }

    //If dateString is in format "yyyyMMdd'T'HHmmss.SSSZ" or its sub-part
    if(isDateFormatForClient(dateString)) {
      //If microseconds are not given append 000 microseconds with Z
      if(dateString.length == 15){
        dateString + ".000Z"
      }
      //If seconds are not given append 00 seconds and 000 microseconds  with Z
      else if (dateString.length == 13) {
        dateString + "00.000Z"
      }
      //If time is given but Z is not, appended it at the end
      else if (dateString.length == 19) {
        dateString + "Z"
      }
      //Considering whole date time String is in correct format. Else date parser will throw error itself.
      else {
        dateString
      }
    }
    //If dateString is in format "yyyy-MM-dd'T'HH:mm:ss.SSSZ" or its sub-part
    //Example: 20171121T054449.918Z
    else if(isDateWithTAndSpecialChars(dateString)) {
      if(dateString.length == 16) {
        dateString + ":00.000Z"
      } else if (dateString.length == 19) {
        dateString + ".000Z"
      } else if (dateString.length == 23) {
        dateString + "Z"
      } else {
        dateString
      }
    }
    //If dateString is in format "yyyy-MM-dd HH:mm:ss.SSSZ" or its sub-part
    else if(isDateWithSpaceAndSpecialChars(dateString)) {
      if(dateString.length == 16) {
        dateString + ":00.000Z"
      } else if (dateString.length == 19) {
        dateString + ".000Z"
      } else if (dateString.length == 23) {
        dateString + "Z"
      } else {
        dateString
      }
    }
    //If dateString is in format yyyy-MM-dd (only date)
    else if (isDateWithSpecialCharsWithoutTime(dateString)) {
      dateString + "T00:00:00.000Z"
    }
    //If dateString is in format yyyyMMdd (only date)
    else if (isDateForClientWithoutTime(dateString)) {
      dateString + "T000000.000Z"
    }
    else {
      dateString
    }
  }

  def isDateFormatForClient(dateString: String): Boolean = {
    dateString.contains('T') && !dateString.contains("-") && !dateString.contains(":")
  }

  def isDateWithTAndSpecialChars(dateString: String): Boolean = {
    dateString.contains('T') && dateString.contains("-") && dateString.contains(":")
  }

  def isDateWithSpecialCharsWithoutTime(dateString: String): Boolean = {
    !dateString.contains('T') && dateString.contains("-") && !dateString.contains(":") && dateString.length == 10
  }

  def isDateForClientWithoutTime(dateString: String): Boolean = {
    !dateString.contains('T') && !dateString.contains("-") && !dateString.contains(":") && dateString.length == 8
  }

  def isDateWithSpaceAndSpecialChars(dateString: String): Boolean = {
    !dateString.contains('T') && dateString.contains("-") && dateString.contains(":") && dateString.contains(" ")
  }

  def splitDateByMonth(startDate: DateTime, endDate: DateTime): List[String] = {
    val months = ((startDate.getMonthOfYear to 12).toList ::: (1 to endDate.getMonthOfYear).toList).distinct
    MonthsHelper.getMonths(months)
  }

}

trait DateTimeWritesWithTAndSpecialChars {
  import DateHelper.formatDateWithTAndSpecialChars
  implicit lazy val dateTimeWrites = new Writes[DateTime] {
    override def writes(date: DateTime): JsValue = {
      JsString(formatDateWithTAndSpecialChars(date)) //Whenever write date to String write it for the end user
    }
  }
}

trait DateTimeWritesForClient {
  import DateHelper.formatDateForEndUser
  implicit lazy val dateTimeWrites = new Writes[DateTime] {
    override def writes(date: DateTime): JsValue = {
      JsString(formatDateForEndUser(date)) //Whenever write date to String write it for the end user
    }
  }
}

trait DateTimeReads {
  import DateHelper.parseDateTimeString
  implicit lazy val dateTimeReads: Reads[DateTime] = new Reads[DateTime] {
    override def reads(json: JsValue): JsResult[DateTime] = json match {
      case date: JsValue ⇒ JsSuccess(parseDateTimeString(date.as[String]))
      case _ ⇒ JsError()
    }
  }
}
