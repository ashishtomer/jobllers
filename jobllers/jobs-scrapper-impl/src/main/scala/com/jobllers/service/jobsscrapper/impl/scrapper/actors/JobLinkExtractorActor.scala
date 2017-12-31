package com.jobllers.service.jobsscrapper.impl.scrapper.actors

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.JobLinkExtractorActor.JobPageCrawlInfo
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.JobLinkExtractorChildActor.CrawlJobPage
import com.jobllers.service.jobsscrapper.impl.scrapper.naukri.Headers
import com.jobllers.service.jobsscrapper.impl.scrapper.util.RemoteContentPuller
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class JobLinkExtractorActor extends Actor with RemoteContentPuller {

  var counter = 0

  override def receive: Receive = {

    case jobPagesCrawlInfo: JobPageCrawlInfo ⇒
      jobPagesCrawlInfo.uriList.foreach { jobPageURI ⇒
        counter = counter + 1
        context.actorOf(JobLinkExtractorChildActor.props, s"JobLinkExtractorActorChild$counter")
          .tell(CrawlJobPage(jobPageURI, jobPagesCrawlInfo.selector), self)
      }

    case links: List[String] ⇒ 
  }
}

object JobLinkExtractorActor {
  val props = Props(classOf[JobLinkExtractorActor])

  case class JobPageCrawlInfo(uriList: List[String], selector: String)

}


class JobLinkExtractorChildActor extends Actor with RemoteContentPuller {

  override def receive: Receive = {

    case CrawlJobPage(uri: String, selector: String) ⇒
      sender ! getJobLinksFromPage(uri, selector, sender())
      self ! PoisonPill

    case _: List[String] ⇒ println("Error: The JobLinkExtractorChildActor is receiving the list of links!")

  }

  private def getJobLinksFromPage(uri: String, selector: String, sender: ActorRef): List[String] = {
    import scala.collection.JavaConversions._
    val page = Jsoup.connect(uri).headers(Headers.naukariHeaders).timeout(60000).get()
    val links = page.select(selector)
      .foldLeft(List.empty[String])((listOfLinks, link) ⇒ addVerifiedLinkToList(listOfLinks, link))

    val nextPageLinkElement: List[Element] = page.select("div.srp_container.fl div.pagination a:last-child").toList
    if (nextPageLinkElement.nonEmpty) {
      nextPageLinkElement.map(paginationEle ⇒ println(s"The pagination button link on $uri is - ${paginationEle.text()}"))
      context.actorOf(JobLinkExtractorChildActor.props)
        .tell(CrawlJobPage(nextPageLinkElement.last.attr("href"), selector), sender)
    }

    links
  }
}

object JobLinkExtractorChildActor {
  val props = Props(classOf[JobLinkExtractorChildActor])

  case class CrawlJobPage(uri: String, selector: String)

}
