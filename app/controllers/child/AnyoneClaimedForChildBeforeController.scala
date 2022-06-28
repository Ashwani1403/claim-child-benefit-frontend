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

package controllers.child

import controllers.AnswerExtractor
import controllers.actions._
import forms.child.AnyoneClaimedForChildBeforeFormProvider
import models.Index
import pages.{RelationshipStatusPage, Waypoints}
import pages.child.{AnyoneClaimedForChildBeforePage, ChildNamePage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.child.AnyoneClaimedForChildBeforeView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AnyoneClaimedForChildBeforeController @Inject()(
                                       override val messagesApi: MessagesApi,
                                       sessionRepository: SessionRepository,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: AnyoneClaimedForChildBeforeFormProvider,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: AnyoneClaimedForChildBeforeView
                                     )(implicit ec: ExecutionContext)
  extends FrontendBaseController
    with I18nSupport
    with AnswerExtractor {

  def onPageLoad(waypoints: Waypoints, index: Index): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      getAnswer(ChildNamePage(index)) {
        childName =>
          getAnswer(RelationshipStatusPage) {
            relationshipStatus =>

              val form = formProvider(childName, relationshipStatus)

              val preparedForm = request.userAnswers.get(AnyoneClaimedForChildBeforePage(index)) match {
                case None => form
                case Some(value) => form.fill(value)
              }

              Ok(view(preparedForm, waypoints, index, childName, relationshipStatus))
            }
      }
  }

  def onSubmit(waypoints: Waypoints, index: Index): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getAnswerAsync(ChildNamePage(index)) {
        childName =>
          getAnswerAsync(RelationshipStatusPage) {
            relationshipStatus =>

              val form = formProvider(childName, relationshipStatus)

              form.bindFromRequest().fold(
                formWithErrors =>
                  Future.successful(BadRequest(view(formWithErrors, waypoints, index, childName, relationshipStatus))),

                value =>
                  for {
                    updatedAnswers <- Future.fromTry(request.userAnswers.set(AnyoneClaimedForChildBeforePage(index), value))
                    _ <- sessionRepository.set(updatedAnswers)
                  } yield Redirect(AnyoneClaimedForChildBeforePage(index).navigate(waypoints, updatedAnswers).route)
              )
          }
      }
  }
}
