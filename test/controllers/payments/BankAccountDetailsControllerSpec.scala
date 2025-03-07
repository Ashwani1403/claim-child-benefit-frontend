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

package controllers.payments

import base.SpecBase
import controllers.{routes => baseRoutes}
import forms.payments.BankAccountDetailsFormProvider
import models.{BankAccountDetails, ReputationResponseEnum, ValidateBankDetailsResponseModel}
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.EmptyWaypoints
import pages.payments.BankAccountDetailsPage
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import services.BarsService
import views.html.payments.BankAccountDetailsView

import scala.concurrent.Future

class BankAccountDetailsControllerSpec extends SpecBase with MockitoSugar {

  val formProvider = new BankAccountDetailsFormProvider()
  val form = formProvider()
  private val waypoints = EmptyWaypoints

  lazy val bankAccountDetailsRoute = routes.BankAccountDetailsController.onPageLoad(waypoints).url

  private val validAnswer = BankAccountDetails("name", "123456", "00123456", None)
  private val userAnswers = emptyUserAnswers.set(BankAccountDetailsPage, validAnswer).success.value

  "BankAccountDetails Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, bankAccountDetailsRoute)

        val view = application.injector.instanceOf[BankAccountDetailsView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, waypoints)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, bankAccountDetailsRoute)

        val view = application.injector.instanceOf[BankAccountDetailsView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(validAnswer), waypoints)(request, messages(application)).toString
      }
    }

    "must save the answer and redirect to the next page when valid data is submitted and the BARS response is successful" in {

      val happyBarsResponse = ValidateBankDetailsResponseModel(
        accountNumberIsWellFormatted = ReputationResponseEnum.Yes,
        nonStandardAccountDetailsRequiredForBacs = ReputationResponseEnum.No,
        sortCodeIsPresentOnEISCD = ReputationResponseEnum.Yes
      )

      val mockBarsService = mock[BarsService]
      val mockSessionRepository = mock[SessionRepository]

      when(mockBarsService.validateBankDetails(any())(any(), any())) thenReturn Future.successful(Some(happyBarsResponse))
      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[SessionRepository].toInstance(mockSessionRepository),
            bind[BarsService].toInstance(mockBarsService)
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, bankAccountDetailsRoute)
            .withFormUrlEncodedBody(("accountName", "name"), ("accountNumber", "00123456"), ("sortCode", "123456"))

        val result = route(application, request).value
        val expectedAnswers = emptyUserAnswers.set(BankAccountDetailsPage, validAnswer).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual BankAccountDetailsPage.navigate(waypoints, emptyUserAnswers, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must save the answer and redirect to the next page when valid data is submitted and we cannot get a good response from BARS" in {

      val mockBarsService = mock[BarsService]
      val mockSessionRepository = mock[SessionRepository]

      when(mockBarsService.validateBankDetails(any())(any(), any())) thenReturn Future.successful(None)
      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[SessionRepository].toInstance(mockSessionRepository),
            bind[BarsService].toInstance(mockBarsService)
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, bankAccountDetailsRoute)
            .withFormUrlEncodedBody(("accountName", "name"), ("accountNumber", "00123456"), ("sortCode", "123456"))

        val result = route(application, request).value
        val expectedAnswers = emptyUserAnswers.set(BankAccountDetailsPage, validAnswer).success.value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual BankAccountDetailsPage.navigate(waypoints, emptyUserAnswers, expectedAnswers).url
        verify(mockSessionRepository, times(1)).set(eqTo(expectedAnswers))
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, bankAccountDetailsRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[BankAccountDetailsView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, waypoints)(request, messages(application)).toString
      }
    }

    "must return a Bad Request when the BARS response indicates a bad sort code" in {

      val invalidDetailsResponse = ValidateBankDetailsResponseModel(
        accountNumberIsWellFormatted = ReputationResponseEnum.Indeterminate,
        nonStandardAccountDetailsRequiredForBacs = ReputationResponseEnum.Indeterminate,
        sortCodeIsPresentOnEISCD = ReputationResponseEnum.No
      )

      val mockBarsService = mock[BarsService]

      when(mockBarsService.validateBankDetails(any())(any(), any())) thenReturn Future.successful(Some(invalidDetailsResponse))

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[BarsService].toInstance(mockBarsService)
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, bankAccountDetailsRoute)
            .withFormUrlEncodedBody(("accountName", "name"), ("accountNumber", "00123456"), ("sortCode", "123456"))

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
      }
    }

    "must return a Bad Request when the BARS response indicates a failed modulus check" in {

      val invalidDetailsResponse = ValidateBankDetailsResponseModel(
        accountNumberIsWellFormatted = ReputationResponseEnum.No,
        nonStandardAccountDetailsRequiredForBacs = ReputationResponseEnum.Indeterminate,
        sortCodeIsPresentOnEISCD = ReputationResponseEnum.Yes
      )

      val mockBarsService = mock[BarsService]

      when(mockBarsService.validateBankDetails(any())(any(), any())) thenReturn Future.successful(Some(invalidDetailsResponse))

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[BarsService].toInstance(mockBarsService)
          )
          .build()

      running(application) {
        val request =
          FakeRequest(POST, bankAccountDetailsRoute)
            .withFormUrlEncodedBody(("accountName", "name"), ("accountNumber", "00123456"), ("sortCode", "123456"))

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, bankAccountDetailsRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, bankAccountDetailsRoute)
            .withFormUrlEncodedBody(("accountName", "name"), ("accountNumber", "00123456"), ("sortCode", "123456"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual baseRoutes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
