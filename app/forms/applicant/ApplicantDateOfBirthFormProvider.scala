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

package forms.applicant

import forms.mappings.Mappings
import play.api.data.Form

import java.time.format.DateTimeFormatter
import java.time.{Clock, LocalDate}
import javax.inject.Inject

class ApplicantDateOfBirthFormProvider @Inject()(clock: Clock) extends Mappings {


  def minDate: LocalDate = LocalDate.now(clock).minusYears(130)
  def maxDate: LocalDate = LocalDate.now(clock)
  private def dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def apply(): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey     = "applicantDateOfBirth.error.invalid",
        allRequiredKey = "applicantDateOfBirth.error.required.all",
        twoRequiredKey = "applicantDateOfBirth.error.required.two",
        requiredKey    = "applicantDateOfBirth.error.required"
      ).verifying(
        maxDate(maxDate, "applicantDateOfBirth.error.afterMaximum", maxDate.format(dateFormatter)),
        minDate(minDate, "applicantDateOfBirth.error.beforeMinimum", minDate.format(dateFormatter))
      )
    )
}
