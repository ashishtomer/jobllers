package com.jobllers.service.jobsscrapper.impl.scrapper

import org.jsoup.Jsoup

//TODO -- Move values stored in NaukriJobSectionsLinks and NaukriSeedLinkSelectors to environment variables
object NaukriJobSectionsLinks {
  val alphabets: List[String] = List[String]("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
    "s", "t", "u", "v", "w", "x", "y", "z", "0-9")
  val byDesignationLinks: List[String] = alphabets.map("https://www.naukri.com/top-jobs-by-designations-" + _)
  val byCompanyLinks: List[String] = alphabets.map("https://www.naukri.com/top-company-jobs-" + _)
  val byTopSkillsLink: String = "https://www.naukri.com/top-skill-jobs"
  val byLocationsLink: String = "https://www.naukri.com/jobs-by-location"
  val byCategoryLinks: String = "https://www.naukri.com/jobs-by-category"
}

object NaukriSeedLinkSelectors {
  val byTopSkillsSelector = "div.colspan_four.collapse a"
  val byLocationSelector = "div.column div.section_white_title ul li a"
  val byDesignationSelector = "div.multiColumn.colCount_four a"
  val byCompanySelector = "div.multiColumn.colCount_four a"
  val byFunctionalAreaCategorySelector = "div.browseFunctionalArea.section_white_title div.multiColumn.colCount_four a"
  val byIndustryCategorySelector = "div.browseIndustry.section_white_title div.multiColumn.colCount_four a"
}

trait NaukriDotComScrapper {

  import NaukriJobSectionsLinks._
  import NaukriSeedLinkSelectors._
  import scala.collection.JavaConversions._

  val naukariHeaders = Map[String, String](
    "scheme" -> "https",
    "accept" -> "text/html",
    "accept-encoding" -> "gzip",
    "accept-language" -> "en-GB,en-US;",
    "user-agent" -> "Mozilla")

  def getJobsSeedLinks = getJobLinksByLocation(byLocationsLink) ++ getTopSkillJobsLinks(byTopSkillsLink) ++
    byDesignationLinks.foldRight(Map.empty[String, String])((link, map) ⇒ map ++ getJobLinksByDesignation(link)) ++
    byCompanyLinks.foldRight(Map.empty[String, String])((link, map) ⇒ map ++ getJobLinksByDesignation(link)) ++
    getJobLinksByFunctionalAreaCategory(byCategoryLinks) ++ getJobLinksByIndustryCategory(byCategoryLinks)

  def getJobLinksByLocation(uri: String): Map[String, String] = extractLinksBySelector(uri, byLocationSelector)

  def getTopSkillJobsLinks(uri: String): Map[String, String] = extractLinksBySelector(uri, byTopSkillsSelector)

  def getJobLinksByDesignation(uri: String): Map[String, String] = extractLinksBySelector(uri, byDesignationSelector)

  def getJobLinksByCompany(uri: String): Map[String, String] = extractLinksBySelector(uri, byCompanySelector)

  def getJobLinksByFunctionalAreaCategory(uri: String): Map[String, String] = extractLinksBySelector(uri, byFunctionalAreaCategorySelector)

  def getJobLinksByIndustryCategory(uri: String): Map[String, String] = extractLinksBySelector(uri, byIndustryCategorySelector)

  private def extractLinksBySelector(uri: String, selectorPattern: String): Map[String, String] =
    Jsoup.connect(uri).headers(naukariHeaders).timeout(60000).get()
      .select(selectorPattern)
      .foldLeft(Map.empty[String, String])((mapOfLinks, link) ⇒
        mapOfLinks + (verifiedLink(link.attr("href")) -> link.attr("title")))

  private def verifiedLink(link: String): String = if (link.matches("(https://www.naukri.com)(.*)")) {
    link
  } else {
    //In case of only the path is returned
    "https://www.naukri.com/" + link
  }

}
