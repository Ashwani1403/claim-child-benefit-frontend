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

package forms.partner

import forms.mappings.Mappings
import play.api.data.Form

import java.time.format.DateTimeFormatter
import java.time.{Clock, LocalDate}
import javax.inject.Inject

class PartnerEldestChildDateOfBirthFormProvider @Inject()(clock: Clock) extends Mappings {

  def minDate: LocalDate    = LocalDate.now(clock).minusYears(20)
  def maxDate: LocalDate    = LocalDate.now(clock)
  private def dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def apply(partnerFirstName: String): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey     = "partnerEldestChildDateOfBirth.error.invalid",
        allRequiredKey = "partnerEldestChildDateOfBirth.error.required.all",
        twoRequiredKey = "partnerEldestChildDateOfBirth.error.required.two",
        requiredKey    = "partnerEldestChildDateOfBirth.error.required",
        args           = Seq(partnerFirstName)
      ).verifying(
        maxDate(maxDate, "partnerEldestChildDateOfBirth.error.afterMaximum", maxDate.format(dateFormatter), partnerFirstName),
        minDate(minDate, "partnerEldestChildDateOfBirth.error.beforeMinimum", minDate.format(dateFormatter), partnerFirstName)
      )
    )
}
