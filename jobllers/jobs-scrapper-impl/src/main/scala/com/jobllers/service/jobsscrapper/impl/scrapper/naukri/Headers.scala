package com.jobllers.service.jobsscrapper.impl.scrapper.naukri

object Headers {

  val naukariHeaders: Map[String, String] = Map[String, String](
    "scheme" -> "https",
    "accept" -> "text/html",
    "accept-encoding" -> "gzip",
    "accept-language" -> "en-GB,en-US;",
    "user-agent" -> "Mozilla")

}
