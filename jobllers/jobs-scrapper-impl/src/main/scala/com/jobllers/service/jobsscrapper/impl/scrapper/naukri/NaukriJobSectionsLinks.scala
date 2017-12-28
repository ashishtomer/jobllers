package com.jobllers.service.jobsscrapper.impl.scrapper.naukri

object NaukriJobSectionsLinks {
  val alphabets: List[String] = List[String]("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
    "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0-9")
  val byDesignationLinks: List[String] = alphabets.map("https://www.naukri.com/top-jobs-by-designations-" + _)
  val byCompanyLinks: List[String] = alphabets.map("https://www.naukri.com/top-company-jobs-" + _)
  val byTopSkillsLink: String = "https://www.naukri.com/top-skill-jobs"
  val byLocationsLink: String = "https://www.naukri.com/jobs-by-location"
  val byCategoryLinks: String = "https://www.naukri.com/jobs-by-category"
}
