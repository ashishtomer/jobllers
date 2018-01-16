package com.jobllers.service.jobscraper.impl.scrapper.actors

import java.io.IOException
import java.util.concurrent.TimeoutException

import akka.actor.{Actor, Props}
import com.jobllers.service.jobscraper.impl.repo.models.ScrapedJobListing
import com.jobllers.service.jobscraper.impl.scrapper.actors.JobDescriptionExtractorChildActor.ChildActorFinished
import com.jobllers.service.jobscraper.impl.scrapper.actors.JobLinkExtractorActor.JobPageCrawlInfo
import com.jobllers.service.jobscraper.impl.scrapper.actors.JobLinkExtractorChildActor.CrawlJobPage
import com.jobllers.service.jobscraper.impl.scrapper.naukri.Headers
import com.jobllers.service.jobscraper.impl.util.RemoteContentPuller
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.util.Try
import scala.util.control.NonFatal

class JobLinkExtractorActor extends Actor {

  var counter = 0

  override def receive: Receive = {

    case jobPagesCrawlInfo: JobPageCrawlInfo ⇒
      jobPagesCrawlInfo.uriList.foreach { jobPageURI ⇒
        counter = counter + 1
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
  var counterJobLinkHandlerActor = 0

  override def receive: Receive = {
    case CrawlJobPage(uri: String, selector: String) ⇒ getJobLinksFromPage(uri, selector)
    case x: Any ⇒ println(s"Something weird received. $x")
  }

  private def getJobLinksFromPage(uri: String, selector: String): Unit = Try {
    import scala.collection.JavaConversions._
    val page = Jsoup.connect(uri).headers(Headers.naukariHeaders).timeout(60000).get()
    val listingElements: List[Element] = page.select(selector).toList

    val nextPageLinkElement: List[Element] = page.select("div.srp_container.fl div.pagination a:last-child").toList

    counterJobLinkHandlerActor += 1

    context.actorOf(JobLinkHandlerActor.props, s"JobLinkHandlerActor$counterJobLinkHandlerActor") ! listingElements

    //Start crawling the "NEXT" page if there are more links else kill itself.
    if (nextPageLinkElement.nonEmpty) {
      //Next Page on the current link found.
      self ! CrawlJobPage(nextPageLinkElement.last.attr("href"), selector)
    } else {
      //Stopping actor. As no more work to do
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

  var counter = 0

  override def receive = {
    case listingElements: List[Element] ⇒
      listingElements.foreach{ element ⇒
//        val linkOfJD = element.child(0)
        val dateOfPosting = element.select("div.rec_details span.date").text().trim.toLowerCase
        if(dateOfPosting.contains("today") || dateOfPosting.contains("1 day ") || dateOfPosting.contains("hour")) {

          val siteJobId: String = element.id()
          val jobTitle = element.select("a.content ul li.desig").text()
          val company = element.select("a.content span.org").text()
          val experience = element.select("a.content span.exp").text()
          val location = element.select("a.content span.loc").text()
          val popularityTag = if(element.select("span.action.jobType").hasClass("premium")) { "Premium Job" }
                else if(element.select("span.action.jobType").hasClass("hotjob")) { "Hot Job" } else { "Normal Job" }
          val salary = element.select("div.other_details span.salary").text()
          val postedBy = if(!element.select("div.other_details div.rec_details a.rec_name").hasAttr("href")) {
              element.select("div.other_details div.rec_details a.rec_name").text()
            } else {
              "@" + element.select("div.other_details div.rec_details a.rec_name").attr("href")
            }
          val bannerAvailable = element.select("div.banner img").attr("src").trim != ""
          val linkWithQueryParams = element.selectFirst("a.content").attr("href")
          val link = linkWithQueryParams.substring(0, linkWithQueryParams.indexOf('?'))

          val scrapedJobListing = ScrapedJobListing(siteJobId, jobTitle, company, experience, location, popularityTag,
            salary, postedBy, bannerAvailable, dateOfPosting, link)

          counter += 1
          context.actorOf(JobDescriptionExtractorActor.props, s"JobDescriptionExtractorActor$counter") ! scrapedJobListing
          println("Link extractor sent the ScrapedJobListing to the JD extractor actor.")

        }
      }

    case ChildActorFinished ⇒
      context.parent ! ChildActorFinished
      context stop self
    case something ⇒ println(s"JobLinkHandlerActor received ${something}")
  }
}

object JobLinkHandlerActor {
  val props = Props(classOf[JobLinkHandlerActor])
  case class JDLinks(links: List[String])
}
