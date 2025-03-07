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
import uk.gov.hmrc.domain.Nino

import javax.inject.Inject
import scala.util.Try

class PartnerNinoFormProvider @Inject() extends Mappings {

  def apply(partnerFirstName: String): Form[Nino] =
    Form(
      "value" -> text("partnerNino.error.required", args = Seq(partnerFirstName))
        .verifying("partnerNino.error.invalid", s => Try(Nino(s.replaceAll("\\s", "").toUpperCase)).isSuccess)
        .transform[Nino](s => Nino(s.replaceAll("\\s", "").toUpperCase), nino => nino.toString)
    )
}
