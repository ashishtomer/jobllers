package com.jobllers.service.jobsscrapper.impl.scrapper.naukri

import com.jobllers.service.jobsscrapper.impl.scrapper.SeedLinkExtractor
import com.jobllers.service.jobsscrapper.impl.scrapper.util.RemoteContentPuller
import org.jsoup.Jsoup

//TODO -- Move values stored in NaukriJobSectionsLinks and NaukriSeedLinkSelectors to environment variables

trait NaukriDotComSeedLinkExtractor extends SeedLinkExtractor with RemoteContentPuller {

  import NaukriJobSectionsLinks._
  import NaukriSeedLinkSelectors._
  implicit val headers =  Headers.naukariHeaders

  def getJobsSeedLinks(): Map[String, String] = getJobLinksByLocation(byLocationsLink) ++
    byDesignationLinks.foldRight(Map.empty[String, String])((link, map) ⇒ map ++ getJobLinksByDesignation(link)) ++
    byCompanyLinks.foldRight(Map.empty[String, String])((link, map) ⇒ map ++ getJobLinksByDesignation(link)) ++
    getJobLinksByFunctionalAreaCategory(byCategoryLinks) ++ getJobLinksByIndustryCategory(byCategoryLinks) ++
    getTopSkillJobsLinks(byTopSkillsLink)

  def getJobLinksByLocation(uri: String): Map[String, String] = extractLinksBySelector(uri, byLocationSelector)

  def getTopSkillJobsLinks(uri: String): Map[String, String] = extractLinksBySelector(uri, byTopSkillsSelector)

  def getJobLinksByDesignation(uri: String): Map[String, String] = extractLinksBySelector(uri, byDesignationSelector)

  def getJobLinksByCompany(uri: String): Map[String, String] = extractLinksBySelector(uri, byCompanySelector)

  def getJobLinksByFunctionalAreaCategory(uri: String): Map[String, String] = extractLinksBySelector(uri, byFunctionalAreaCategorySelector)

  def getJobLinksByIndustryCategory(uri: String): Map[String, String] = extractLinksBySelector(uri, byIndustryCategorySelector)

}
