package com.jobllers.service.jobsscrapper.impl.util

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

trait RemoteContentPuller {

  import scala.collection.JavaConversions._

  private val linkRegex = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+" +
    "([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$"

  def extractLinksBySelector(uri: String, selectorPattern: String)
                            (implicit requestHeaders: Map[String, String]): List[String] =
    getLinksFromPage(getPage(uri), selectorPattern)

  def getPage(uri: String)(implicit requestHeaders: Map[String, String]): Document =
    Jsoup.connect(uri).headers(requestHeaders).timeout(60000).get()

  def getLinksFromPage(page: Document, selectorPattern: String) = page.select(selectorPattern)
    .foldLeft(List.empty[String])((listOfLinks, link) ⇒ addVerifiedLinkToList(listOfLinks, link))

  def addVerifiedLinkToList(listOfLinks: List[String], link: Element): List[String] =
    if (link.attr("href").matches(linkRegex))
      link.attr("href") +: listOfLinks
    else listOfLinks

}
