package com.jobllers.service.jobsscrapper.impl.scrapper.util

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

trait RemoteContentPuller {

  import scala.collection.JavaConversions._

  private val linkRegex = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+" +
    "([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$"

  def extractLinksBySelector(uri: String, selectorPattern: String)(implicit requestHeaders: Map[String, String]) =
    getPage(uri).select(selectorPattern)
      .foldLeft(Map.empty[String, String])((mapOfLinks, link) ⇒ createLinkMap(link) ++ mapOfLinks)

  def getPage(uri: String)(implicit requestHeaders: Map[String, String]) =
    Jsoup.connect(uri).headers(requestHeaders).timeout(60000).get()

  def getLinksFromPage(page: Document, selectorPattern: String) = page.select(selectorPattern)
    .foldLeft(Map.empty[String, String])((mapOfLinks, link) ⇒ createLinkMap(link) ++ mapOfLinks)

  private def createLinkMap(link: Element): Map[String, String] =
    if (link.attr("href").matches(linkRegex)) Map(link.attr("href") -> link.attr("title"))
    else Map.empty[String, String]

}
