package com.jobllers.service.jobsscrapper.impl.scrapper.actors

import akka.actor.{Actor, PoisonPill, Props}
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.JobLinkExtractorActor.JobPageCrawlInfo
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.SeedLinkExtractorActor.{FileName, SeedPage, SeedPageCSVDirectory, SeedPageOptional}
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.SeedLinkExtractorActorChild.{GetSeedLinksFromPage, SendSeedLinks}
import com.jobllers.service.jobsscrapper.impl.scrapper.naukri.Headers
import com.jobllers.service.jobsscrapper.impl.util.RemoteContentPuller
import org.joda.time.DateTime
import org.jsoup.Jsoup

class SeedLinkExtractorActor extends Actor {

  var numberOfSeedLinks = 0
  var startingMillisec: Long = 0L
  var jobLinkExtractorActorCounter = 0

  override def receive: Receive = {

    case SeedPageCSVDirectory(csvFile: FileName) ⇒ self ! readSeedPagesFromCSVDiectory(csvFile)

    case seedPages: List[SeedPageOptional] ⇒
      var counter = 0
      startingMillisec = DateTime.now().getMillis

      seedPages.foreach { seedPageOpt ⇒
        counter += 1
        context.actorOf(Props(classOf[SeedLinkExtractorActorChild]), s"childOfSeedLinkExtractorActor$counter")
          .tell(GetSeedLinksFromPage(seedPageOpt.optSeedPage.get), self)
      }

    case sentSeedLinks: SendSeedLinks ⇒
      val seedLinks = sentSeedLinks.linkList
      jobLinkExtractorActorCounter += 1
      context.actorOf(JobLinkExtractorActor.props, s"jobLinkExtractorActor$jobLinkExtractorActorCounter")
        .tell(JobPageCrawlInfo(seedLinks, "div.srp_container.fl div.row"), self)
      numberOfSeedLinks += seedLinks.size
      println(s"$numberOfSeedLinks links extracted in ${DateTime.now().getMillis - startingMillisec} milliseconds")

  }

  private def readSeedPagesFromCSVDiectory(fileName: FileName): List[SeedPageOptional] = {
    import scala.io.Source
    val csvDirectorySource = Source.fromFile(fileName.name)
    val linesFromDirectory = csvDirectorySource.getLines().toList
    csvDirectorySource.close()

    linesFromDirectory.map { line =>
      val linkSelector = line.split(",")
      if (linkSelector.size == 2) {
        SeedPageOptional(Some(SeedPage(linkSelector.head, linkSelector(1))))
      } else {
        SeedPageOptional(None)
      }
    }
  }
}

object SeedLinkExtractorActor {

  val props = Props(classOf[SeedLinkExtractorActor])

  case class FileName(name: String)
  case class SeedPage(uri: String, selector: String)
  case class SeedPageOptional(optSeedPage: Option[SeedPage])
  case class SeedPageList(seedPages: List[Option[SeedPage]])
  case class SeedPageCSVDirectory(file: FileName)

}

class SeedLinkExtractorActorChild extends Actor with RemoteContentPuller {

  import scala.collection.JavaConversions._

  var numberOfSeedLinks = 0

  override def receive: Receive = {
    case GetSeedLinksFromPage(seedPage: SeedPage) ⇒
      val seedLinks = Jsoup.connect(seedPage.uri).headers(Headers.naukariHeaders).timeout(60000).get()
        .select(seedPage.selector)
        .foldLeft(List.empty[String])((listOfLinks, link) ⇒ addVerifiedLinkToList(listOfLinks, link))

      sender() ! SendSeedLinks(seedLinks)
      self ! PoisonPill
  }
}

object SeedLinkExtractorActorChild {

  case class SendSeedLinks(linkList: List[String])
  case class GetSeedLinksFromPage(seedPage: SeedPage)

}
