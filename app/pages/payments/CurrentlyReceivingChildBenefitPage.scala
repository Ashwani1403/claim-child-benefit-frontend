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

package pages.payments

import controllers.payments.routes
import models.UserAnswers
import pages.{Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

case object CurrentlyReceivingChildBenefitPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "currentlyReceivingChildBenefit"

  override def route(waypoints: Waypoints): Call =
    routes.CurrentlyReceivingChildBenefitController.onPageLoad(waypoints)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case true  => EldestChildNamePage
      case false => WantToBePaidPage
    }.orRecover

  override def cleanup(value: Option[Boolean], userAnswers: UserAnswers): Try[UserAnswers] =
    value.map {
      case true =>
        userAnswers.remove(WantToBePaidPage)
          .flatMap(_.remove(PaymentFrequencyPage))

      case false =>
        userAnswers.remove(EldestChildNamePage)
          .flatMap(_.remove(EldestChildDateOfBirthPage))
          .flatMap(_.remove(WantToBePaidToExistingAccountPage))

    }.getOrElse(super.cleanup(value, userAnswers))
}
