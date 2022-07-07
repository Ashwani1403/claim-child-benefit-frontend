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

package pages.child

import controllers.child.routes
import models.ApplicantRelationshipToChild._
import models.{ApplicantRelationshipToChild, Index, UserAnswers}
import pages.{Page, Waypoints}
import play.api.libs.json.JsPath
import play.api.mvc.Call

import scala.util.Try

final case class ApplicantRelationshipToChildPage(index: Index) extends ChildQuestionPage[ApplicantRelationshipToChild] {

  override def path: JsPath = JsPath \ "children" \ index.position \ toString

  override def toString: String = "applicantRelationshipToChild"

  override def route(waypoints: Waypoints): Call =
    routes.ApplicantRelationshipToChildController.onPageLoad(waypoints, index)

  override protected def nextPageNormalMode(waypoints: Waypoints, answers: UserAnswers): Page =
    answers.get(this).map {
      case AdoptingChild =>
        AdoptingChildPage(index)

      case _ =>
        AnyoneClaimedForChildBeforePage(index)
    }.orRecover

  override def cleanup(value: Option[ApplicantRelationshipToChild], userAnswers: UserAnswers): Try[UserAnswers] =
    if (value.contains(AdoptingChild)) {
      super.cleanup(value, userAnswers)
    } else {
      userAnswers.remove(AdoptingChildPage(index))
    }
}
