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
import models.{Address, AdultName}
import play.api.data.Form
import play.api.data.Forms._

import javax.inject.Inject

class PreviousClaimantAddressFormProvider @Inject() extends Mappings {

  def apply(previousClaimant: AdultName): Form[Address] = Form(
    mapping(
      "line1" -> text("previousClaimantAddress.error.line1.required", args = Seq(previousClaimant.firstName))
        .verifying(maxLength(100, "previousClaimantAddress.error.line1.length", previousClaimant.firstName)),
      "line2" -> optional(text("previousClaimantAddress.error.line2.required", args = Seq(previousClaimant.firstName))
        .verifying(maxLength(100, "previousClaimantAddress.error.line2.length", previousClaimant.firstName))),
      "town" -> text("previousClaimantAddress.error.town.required", args = Seq(previousClaimant.firstName))
        .verifying(maxLength(100, "previousClaimantAddress.error.town.length", previousClaimant.firstName)),
      "county" -> optional(text("previousClaimantAddress.error.county.required", args = Seq(previousClaimant.firstName))
        .verifying(maxLength(100, "previousClaimantAddress.error.county.length", previousClaimant.firstName))),
      "postcode" -> text("previousClaimantAddress.error.postcode.required", args = Seq(previousClaimant.firstName))
        .verifying(maxLength(100, "previousClaimantAddress.error.postcode.length", previousClaimant.firstName))
    )(Address.apply)(Address.unapply)
  )
}
