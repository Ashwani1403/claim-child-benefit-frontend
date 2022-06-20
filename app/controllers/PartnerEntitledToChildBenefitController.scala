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

package controllers

import controllers.actions._
import forms.PartnerEntitledToChildBenefitFormProvider

import javax.inject.Inject
import pages.{PartnerEntitledToChildBenefitPage, PartnerNamePage, Waypoints}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import play.twirl.api.HtmlFormat
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.PartnerEntitledToChildBenefitView

import scala.concurrent.{ExecutionContext, Future}

class PartnerEntitledToChildBenefitController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: PartnerEntitledToChildBenefitFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: PartnerEntitledToChildBenefitView
                                 )(implicit ec: ExecutionContext)
  extends FrontendBaseController
    with I18nSupport
    with AnswerExtractor {

  def onPageLoad(waypoints: Waypoints): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      getAnswer(PartnerNamePage) {
        partnerName =>

          val safeFirstName = HtmlFormat.escape(partnerName.firstName).toString
          val form          = formProvider(safeFirstName)

          val preparedForm = request.userAnswers.get(PartnerEntitledToChildBenefitPage) match {
            case None => form
            case Some(value) => form.fill(value)
          }

          Ok(view(preparedForm, waypoints, safeFirstName))
      }
  }

  def onSubmit(waypoints: Waypoints): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      getAnswerAsync(PartnerNamePage) {
        partnerName =>

          val safeFirstName = HtmlFormat.escape(partnerName.firstName).toString
          val form          = formProvider(safeFirstName)

          form.bindFromRequest().fold(
            formWithErrors =>
              Future.successful(BadRequest(view(formWithErrors, waypoints, safeFirstName))),

            value =>
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.set(PartnerEntitledToChildBenefitPage, value))
                _ <- sessionRepository.set(updatedAnswers)
              } yield Redirect(PartnerEntitledToChildBenefitPage.navigate(waypoints, updatedAnswers))
          )
      }
  }
}