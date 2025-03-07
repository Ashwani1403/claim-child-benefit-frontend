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

package pages.income

import controllers.income.routes
import models.{Benefits, UserAnswers}
import pages.payments.{WantToBePaidPage, PaymentFrequencyPage}
import pages.{NonEmptyWaypoints, Page, QuestionPage, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

case object ApplicantBenefitsPage extends QuestionPage[Set[Benefits]] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "applicantBenefits"

  override def route(waypoints: Waypoints): Call =
    routes.ApplicantBenefitsController.onPageLoad(waypoints)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    TaxChargeExplanationPage

  override protected def nextPageCheckMode(waypoints: NonEmptyWaypoints, answers: UserAnswers): Page =
    answers.get(WantToBePaidPage).map {
      case true  => TaxChargeExplanationPage
      case false => waypoints.next.page
    }.getOrElse(waypoints.next.page)
}
