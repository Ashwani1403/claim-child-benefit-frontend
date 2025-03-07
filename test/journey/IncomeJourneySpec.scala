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

package journey

import models.Benefits
import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import pages.income._
import pages.payments.CurrentlyReceivingChildBenefitPage

class IncomeJourneySpec extends AnyFreeSpec with JourneyHelpers {

  "users with a partner" - {

    val benefits = Gen.nonEmptyListOf(Gen.oneOf(Benefits.values)).map(_.toSet).sample.value

    "and an income below 50k must proceed to the payments section" in {

      startingFrom(ApplicantOrPartnerIncomeOver50kPage)
        .run(
          submitAnswer(ApplicantOrPartnerIncomeOver50kPage, false),
          submitAnswer(ApplicantOrPartnerBenefitsPage, benefits),
          pageMustBe(TaxChargeExplanationPage),
          next,
          pageMustBe(CurrentlyReceivingChildBenefitPage)
        )
    }

    "and an income between 50k and 60k must proceed to the payments section" in {

      startingFrom(ApplicantOrPartnerIncomeOver50kPage)
        .run(
          submitAnswer(ApplicantOrPartnerIncomeOver50kPage, true),
          submitAnswer(ApplicantOrPartnerIncomeOver60kPage, false),
          submitAnswer(ApplicantOrPartnerBenefitsPage, benefits),
          pageMustBe(TaxChargeExplanationPage),
          next,
          pageMustBe(CurrentlyReceivingChildBenefitPage)
        )
    }

    "and an income above 60k must proceed to the payments section" in {

      startingFrom(ApplicantOrPartnerIncomeOver50kPage)
        .run(
          submitAnswer(ApplicantOrPartnerIncomeOver50kPage, true),
          submitAnswer(ApplicantOrPartnerIncomeOver60kPage, true),
          submitAnswer(ApplicantOrPartnerBenefitsPage, benefits),
          pageMustBe(TaxChargeExplanationPage),
          next,
          pageMustBe(CurrentlyReceivingChildBenefitPage)
        )
    }
  }

  "users without a partner" - {

    val benefits = Gen.nonEmptyListOf(Gen.oneOf(Benefits.values)).map(_.toSet).sample.value

    "and an income below 50k must proceed to the payments section" in {

      startingFrom(ApplicantIncomeOver50kPage)
        .run(
          submitAnswer(ApplicantIncomeOver50kPage, false),
          submitAnswer(ApplicantBenefitsPage, benefits),
          pageMustBe(TaxChargeExplanationPage),
          next,
          pageMustBe(CurrentlyReceivingChildBenefitPage)
        )
    }

    "and an income between 50k and 60k must proceed to the payments section" in {

      startingFrom(ApplicantIncomeOver50kPage)
        .run(
          submitAnswer(ApplicantIncomeOver50kPage, true),
          submitAnswer(ApplicantIncomeOver60kPage, false),
          submitAnswer(ApplicantBenefitsPage, benefits),
          pageMustBe(TaxChargeExplanationPage),
          next,
          pageMustBe(CurrentlyReceivingChildBenefitPage)
        )
    }

    "and an income above 60k must proceed to the payments section" in {

      startingFrom(ApplicantIncomeOver50kPage)
        .run(
          submitAnswer(ApplicantIncomeOver50kPage, true),
          submitAnswer(ApplicantIncomeOver60kPage, true),
          submitAnswer(ApplicantBenefitsPage, benefits),pageMustBe(TaxChargeExplanationPage),
          next,
          pageMustBe(CurrentlyReceivingChildBenefitPage)
        )
    }
  }
}
