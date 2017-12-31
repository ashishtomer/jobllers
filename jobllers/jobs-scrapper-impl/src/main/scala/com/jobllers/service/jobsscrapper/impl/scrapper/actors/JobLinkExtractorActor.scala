package com.jobllers.service.jobsscrapper.impl.scrapper.actors

import java.io.IOException
import java.util.concurrent.TimeoutException

import akka.actor.{Actor, Props}
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.JobLinkExtractorActor.JobPageCrawlInfo
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.JobLinkExtractorChildActor.CrawlJobPage
import com.jobllers.service.jobsscrapper.impl.scrapper.naukri.Headers
import com.jobllers.service.jobsscrapper.impl.scrapper.util.RemoteContentPuller
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.util.Try
import scala.util.control.NonFatal

class JobLinkExtractorActor extends Actor with RemoteContentPuller {

  var counter = 0

  override def receive: Receive = {

    case jobPagesCrawlInfo: JobPageCrawlInfo ⇒
      jobPagesCrawlInfo.uriList.foreach { jobPageURI ⇒
        counter = counter + 1
        println(s"[~~~~~~~~~~~~~~~~~~~~~~~~~~~] -> Listing will extracted from listing page.")
        context.actorOf(JobLinkExtractorChildActor.props, s"JobLinkExtractorActorChild$counter")
          .tell(CrawlJobPage(jobPageURI, jobPagesCrawlInfo.selector), self)
      }

  }
}

object JobLinkExtractorActor {
  val props = Props(classOf[JobLinkExtractorActor])

  case class JobPageCrawlInfo(uriList: List[String], selector: String)

}


class JobLinkExtractorChildActor extends Actor with RemoteContentPuller {

  var retryCount = 0

  override def receive: Receive = {
    case CrawlJobPage(uri: String, selector: String) ⇒ getJobLinksFromPage(uri, selector)
    case x: Any ⇒ println(s"Something weird received. $x")
  }

  private def getJobLinksFromPage(uri: String, selector: String): Unit = Try {
    import scala.collection.JavaConversions._
    val page = Jsoup.connect(uri).headers(Headers.naukariHeaders).timeout(60000).get()
    val links = page.select(selector)
      .foldLeft(List.empty[String])((listOfLinks, link) ⇒ addVerifiedLinkToList(listOfLinks, link))

    val nextPageLinkElement: List[Element] = page.select("div.srp_container.fl div.pagination a:last-child").toList

    context.actorOf(JobLinkHandlerActor.props) ! links

    //Start crawling the "NEXT" page if there are more links else kill itself.
    if (nextPageLinkElement.nonEmpty) {
      println(s"-> -> -> -> Next on link found.")
      self ! CrawlJobPage(nextPageLinkElement.last.attr("href"), selector)
    } else {
      println(s". . . . Stopping actor.")
      context stop self
    }
  } recover {
    case NonFatal(_: TimeoutException) ⇒
      if (retryCount < 5) {
        println(s"Couldn't connect to the $uri while trying for 60 seconds. Retrying.")
        self ! CrawlJobPage(uri, selector)
      } else {
        context stop self
      }
      retryCount += 1
    case NonFatal(_: IOException) ⇒
      println(s"Got IOException while extracting job links from $uri")
      context stop self
  }
}

object JobLinkExtractorChildActor {
  val props = Props(classOf[JobLinkExtractorChildActor])

  case class CrawlJobPage(uri: String, selector: String)

}

class JobLinkHandlerActor extends Actor {
  override def receive = {
    case links: List[String] ⇒
      //Save these links into the database / file
      println(s"************************* ${links.size} links received successfully *************************")
  }
}

object JobLinkHandlerActor {
  val props = Props(classOf[JobLinkHandlerActor])
}
