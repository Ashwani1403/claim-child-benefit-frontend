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

package forms.income

import forms.mappings.Mappings
import models.Benefits
import play.api.data.Form
import play.api.data.Forms.set

import javax.inject.Inject

class ApplicantBenefitsFormProvider @Inject() extends Mappings {

  def apply(): Form[Set[Benefits]] =
    Form(
      "value" ->
        set(enumerable[Benefits]("applicantBenefits.error.required"))
          .verifying(
            nonEmptySet("applicantBenefits.error.required"),
            noMutuallyExclusiveAnswers[Benefits](
              Benefits.qualifyingBenefits,
              Set(Benefits.NoneOfTheAbove),
              "applicantBenefits.error.mutuallyExclusive"
            )
          )
    )
}
