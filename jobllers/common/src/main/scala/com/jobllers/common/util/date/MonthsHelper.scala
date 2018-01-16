package com.jobllers.common.util.date

import org.joda.time.DateTime

object MonthsHelper {

  lazy val monthsIntToString: Map[Int, String] = Map((1, "January"), (2, "February"), (3, "March"), (4, "April"), (5, "May"),
    (6, "June"), (7, "July"), (8, "August"), (9, "September"), (10, "October"), (11, "November"), (12, "December"))

  lazy val allMonths: List[String] = (1 to 12).map(number ⇒ monthsIntToString(number)).toList

  def getMonths(monthsNumber: List[Int]): List[String] = {
    monthsNumber.map(number ⇒ monthsIntToString(number))
  }
}

trait DateRangeSplitter {
  /**
    * Returns the distinct names of months between startDate and endDate.
    */
  def getMonthsOfDateRange(startDate: DateTime, endDate: DateTime): List[String] = {
    val months = ((startDate.getMonthOfYear to 12).toList ::: (1 to endDate.getMonthOfYear).toList).distinct
    MonthsHelper.getMonths(months)
  }
}
