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

package forms.child

import forms.mappings.Mappings
import models.AdultName
import play.api.data.Form
import play.api.data.Forms._

import javax.inject.Inject

class PreviousClaimantNameFormProvider @Inject() extends Mappings {

  def apply(): Form[AdultName] = Form(
    mapping(
      "title" -> optional(text("previousClaimantName.error.title.required")
        .verifying(maxLength(20, "previousClaimantName.error.title.length"))),
      "firstName" -> text("previousClaimantName.error.firstName.required")
        .verifying(maxLength(100, "previousClaimantName.error.firstName.length")),
      "middleNames" -> optional(text("previousClaimantName.error.middleNames.required")
        .verifying(maxLength(100, "previousClaimantName.error.middleNames.length"))),
      "lastName" -> text("previousClaimantName.error.lastName.required")
        .verifying(maxLength(100, "previousClaimantName.error.lastName.length"))
    )(AdultName.apply)(AdultName.unapply)
  )
}
