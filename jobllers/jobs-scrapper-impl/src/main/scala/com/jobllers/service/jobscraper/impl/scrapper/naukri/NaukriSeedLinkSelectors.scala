package com.jobllers.service.jobscraper.impl.scrapper.naukri

object NaukriSeedLinkSelectors {
  val byTopSkillsSelector = "div.colspan_four.collapse a"
  val byLocationSelector = "div.column div.section_white_title ul li a"
  val byDesignationSelector = "div.multiColumn.colCount_four a"
  val byCompanySelector = "div.multiColumn.colCount_four a"
  val byFunctionalAreaCategorySelector = "div.browseFunctionalArea.section_white_title div.multiColumn.colCount_four a"
  val byIndustryCategorySelector = "div.browseIndustry.section_white_title div.multiColumn.colCount_four a"
}
