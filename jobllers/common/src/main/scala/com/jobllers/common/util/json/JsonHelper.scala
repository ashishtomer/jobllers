package com.jobllers.common.util.json

import play.api.libs.json.{Format, JsValue, Json}
import java.util.regex.Pattern

trait JsonHelper {

  def getJsonString(jsValue: JsValue): String = Json.stringify(jsValue)
  def getJsonPrettyString(jsValue: JsValue): String = Json.prettyPrint(jsValue)
  def getJsonString[T](obj: T)(implicit format: Format[T]): String = getJsonString(Json.toJson(obj))
  def getJsValue[T](obj: T)(implicit format: Format[T]): JsValue = Json.toJson(obj)
  def parseJsonToObject[T](jsonStr: String)(implicit format: Format[T]): T = Json.parse(jsonStr).as[T]

  def getJsonStringWithSnakeKeys(jsonString: String): String = camelToSnakeCaseKeyJson(jsonString)
  def getJsonStringWithSnakeKeys[T](obj: T)(implicit format: Format[T]): String = camelToSnakeCaseKeyJson(getJsonString(obj))

  private def camelToSnakeCaseKeyJson(json: String): String = {
    var lcaseStr = json
    val pattern = Pattern.compile("(\"[ ]*[a-zA-Z_]+[a-zA-Z0-9_]*[ ]*\"[ ]*:)")
    val matcher = pattern.matcher(json)

    while (matcher.find())
      lcaseStr = lcaseStr.replace(matcher.group(0), toSnakeCase(matcher.group(0)))
    lcaseStr
  }

  private def toSnakeCase(camelCase: String): String =
    camelCase.charAt(0) + camelCase.substring(1,2).toLowerCase() + camelCase.substring(2).trim
      .replace("A", "_a")
      .replace("B", "_b")
      .replace("C", "_c")
      .replace("D", "_d")
      .replace("E", "_e")
      .replace("F", "_f")
      .replace("G", "_g")
      .replace("H", "_h")
      .replace("I", "_i")
      .replace("J", "_j")
      .replace("K", "_k")
      .replace("L", "_l")
      .replace("M", "_m")
      .replace("N", "_n")
      .replace("O", "_o")
      .replace("P", "_p")
      .replace("Q", "_q")
      .replace("R", "_r")
      .replace("S", "_s")
      .replace("T", "_t")
      .replace("U", "_u")
      .replace("V", "_v")
      .replace("W", "_w")
      .replace("X", "_x")
      .replace("Y", "_y")
      .replace("Z", "_z")
      .replace(" ", "")

  def camelifyJsonKeys(jsonString: String): String = {
    var lcaseStr = jsonString
    val pattern = Pattern.compile("(\"[ ]*[a-z_]+[a-z0-9_]*[ ]*\"[ ]*:)") //Keeping in mind the identifier naming convention of Java
    val matcher = pattern.matcher(lcaseStr)

    while (matcher.find())
      lcaseStr = lcaseStr.replace(matcher.group(0), toCamelCaseKeys(matcher.group(0)))
    lcaseStr
  }

  private def toCamelCaseKeys(camelCase: String): String =
    camelCase.charAt(0) + camelCase.substring(1).trim
      .replace("_a", "A")
      .replace("_b", "B")
      .replace("_c", "C")
      .replace("_d", "D")
      .replace("_e", "E")
      .replace("_f", "F")
      .replace("_g", "G")
      .replace("_h", "H")
      .replace("_i", "I")
      .replace("_j", "J")
      .replace("_k", "K")
      .replace("_l", "L")
      .replace("_m", "M")
      .replace("_n", "N")
      .replace("_o", "O")
      .replace("_p", "P")
      .replace("_q", "Q")
      .replace("_r", "R")
      .replace("_s", "S")
      .replace("_t", "T")
      .replace("_u", "U")
      .replace("_v", "V")
      .replace("_w", "W")
      .replace("_x", "X")
      .replace("_y", "Y")
      .replace("_z", "Z")
      .replace(" ", "")
}
