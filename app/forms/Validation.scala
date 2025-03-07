/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package forms

import scala.util.matching.Regex

object Validation {

  val accountNumberPattern: Regex                  = "[ -]*(?:\\d[ -]*){6,8}".r.anchored
  val sortCodePattern: Regex                       = "[ -]*(?:\\d[ -]*){6}".r.anchored
  val rollNumberPattern: Regex                     = """[a-zA-Z0-9- ./]{1,18}""".r.anchored
  val systemNumberPattern: Regex                   = "(?:\\d[ -]*){9}".r.anchored
  val scottishBirthCertificateNumberPattern: Regex = "[ -]*(?:\\d[ -]*){10}".r.anchored
}
