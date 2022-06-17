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

package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import viewmodels.govuk.checkbox._

sealed trait PartnerEmploymentStatus

object PartnerEmploymentStatus extends Enumerable.Implicits {

  case object LookingForWork extends WithName("lookingForWork") with PartnerEmploymentStatus
  case object Employed extends WithName("employed") with PartnerEmploymentStatus
  case object SelfEmployed extends WithName("selfEmployed") with PartnerEmploymentStatus
  case object NoneOfThese extends WithName("noneOfThese") with PartnerEmploymentStatus

  val values: Seq[PartnerEmploymentStatus] = Seq(
    LookingForWork,
    Employed,
    SelfEmployed,
    NoneOfThese
  )

  val activeStatuses: Set[PartnerEmploymentStatus] = values.toSet - NoneOfThese

  def checkboxItems(implicit messages: Messages): Seq[CheckboxItem] = {

    val divider = CheckboxItem(divider = Some(messages("site.or")))

    values.zipWithIndex.map {
      case (value, index) =>
        CheckboxItemViewModel(
          content = Text(messages(s"partnerEmploymentStatus.${value.toString}")),
          fieldId = "value",
          index = index,
          value = value.toString
        )
    }.patch(activeStatuses.size, List(divider), 0)
  }

  implicit val enumerable: Enumerable[PartnerEmploymentStatus] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
