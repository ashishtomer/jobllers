package com.jobllers.service.jobsscrapper.impl.scrapper.actors

import akka.actor.{Actor, Props}
import com.jobllers.service.jobsscrapper.impl.repo.models.ScrapedJobListing
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.JobDescriptionExtractorChildActor.ChildActorFinished
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.JobLinkHandlerActor.JDLinks
import com.jobllers.service.jobsscrapper.impl.scrapper.naukri.Headers
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


class JobDescriptionExtractorActor extends Actor {
  var counter = 0

  override def receive = {
    case jdLinks: JDLinks ⇒
      println("\nThe JDLinks received\n")
      jdLinks.links.foreach { uri ⇒
        counter += 1
        context.actorOf(JobDescriptionExtractorChildActor.props, s"JobDescriptionExtractorChildActor$counter") ! uri
      }

    case ChildActorFinished ⇒ counter -= 1
      if (counter == 0) {
        context.parent ! ChildActorFinished
        context stop self
      }

    case sJL: ScrapedJobListing ⇒ val jdPage = Jsoup.connect(sJL.jobURL).timeout(60000).get()
      val extranctedData = if (jdPage.select("").size() > 0) {
        extractDataFromType1Page(jdPage, sJL)
      } else if (jdPage.select("table.jobsTable").size() > 0) {
        extractDataFromType3Page(jdPage, sJL)
      } else if (jdPage.select("div#jdDic").size() > 0) {
        extractDataFromType2Page(jdPage, sJL)
      } else {
        println(s"Can't understand the JD page for URL -- ${sJL.jobURL}")
        throw new RuntimeException(s"Can't understand the JD page for URL -- ${sJL.jobURL}")
      }

    case someThing ⇒ println(s"The JobDescriptionExtractorActor received -- $someThing")

  }

  private def extractDataFromType1Page(jdPage: Element, sJL: ScrapedJobListing): Unit = {
    println(s"Type 1 - ${sJL.jobURL}")
  }

  private def extractDataFromType2Page(jdPage: Element, sJL: ScrapedJobListing): Unit = {
    println(s"Type 2 - ${sJL.jobURL}")
  }

  private def extractDataFromType3Page(jdPage: Element, sJL: ScrapedJobListing): Unit = {
    println(s"Type 3 - ${sJL.jobURL}")
  }
}

object JobDescriptionExtractorActor {
  val props = Props(classOf[JobDescriptionExtractorActor])
}

class JobDescriptionExtractorChildActor extends Actor {
  override def receive = {
    case uri: String ⇒

      println(s"Extracting the jobs' description for uri -- $uri")

      import scala.collection.JavaConversions._
      val page = Jsoup.connect(uri).headers(Headers.naukariHeaders).timeout(60000).get()
      println("\nThe body of the JOB Description is -\n")
      println(s"\nThe page content is --- \n${page.body().text()}\n")
      val experience = page.select("div.hdSec > div.p span").text()
      val location = page.select("div.hdSec div.loc").text()
      val companyName = page.select("div.hdSec a#jdCpName.pHead").text()
      val jobTitle = page.select("div.hdSec h1.small_title").text()
      val salary = page.select("div.sumFoot span:nth-child(2)").text
      val jobDescription: List[String] = page.select("div.jDisc.mt20 p").toList.map(_.text)
      val keySkills = (page.select("div.ksTags a").toList ::: page.select("div.ksTags span").toList).map(_.text)
      val candidateProfile = page.select("div.jDisc.edu p").toList.map(_.text)
      println(s"Experience - $experience")
      println(s"location - $location")
      println(s"companyName - $companyName")
      println(s"jobTitle - $jobTitle")
      println(s"salary - $salary")
      println(s"jobDescription - ${jobDescription.mkString("\n")}")
      println(s"keySkills - ${keySkills.mkString("\n")}")
      println(s"candidateProfile - ${candidateProfile.mkString("\n")}")
      sender ! ChildActorFinished
  }
}

object JobDescriptionExtractorChildActor {
  val props = Props(classOf[JobDescriptionExtractorChildActor])

  case object ChildActorFinished

}
