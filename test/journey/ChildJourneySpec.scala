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

import generators.ModelGenerators
import models.ChildBirthRegistrationCountry.{England, NorthernIreland, Other, Scotland, Unknown, Wales}
import models._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import pages.child._

import java.time.LocalDate

class ChildJourneySpec extends AnyFreeSpec with JourneyHelpers with ModelGenerators {

  "users whose child has no previous names must not be asked for them" in {

    val childName = ChildName("first", None, "last")
    val sex = arbitrary[ChildBiologicalSex].sample.value

    startingFrom(ChildNamePage(Index(0)))
      .run(
        submitAnswer(ChildNamePage(Index(0)), childName),
        submitAnswer(ChildHasPreviousNamePage(Index(0)), false),
        submitAnswer(ChildBiologicalSexPage(Index(0)), sex),
        submitAnswer(ChildDateOfBirthPage(Index(0)), LocalDate.now),
        pageMustBe(ChildBirthRegistrationCountryPage(Index(0)))
      )
  }

  "users whose child has previous names must be asked for as many as necessary" in {

    val childName = ChildName("first", None, "last")

    startingFrom(ChildNamePage(Index(0)))
      .run(
        submitAnswer(ChildNamePage(Index(0)), childName),
        submitAnswer(ChildHasPreviousNamePage(Index(0)), true),
        submitAnswer(ChildNameChangedByDeedPollPage(Index(0)), true),
        submitAnswer(ChildPreviousNamePage(Index(0), Index(0)), childName),
        submitAnswer(AddChildPreviousNamePage(Index(0)), true),
        submitAnswer(ChildPreviousNamePage(Index(0), Index(1)), childName),
        submitAnswer(AddChildPreviousNamePage(Index(0)), false),
        pageMustBe(ChildBiologicalSexPage(Index(0)))
      )
  }

  "users whose child has previous names must be able to remove them" in {

    val childName = ChildName("first", None, "last")

    startingFrom(ChildNamePage(Index(0)))
      .run(
        submitAnswer(ChildNamePage(Index(0)), childName),
        submitAnswer(ChildHasPreviousNamePage(Index(0)), true),
        submitAnswer(ChildNameChangedByDeedPollPage(Index(0)), true),
        submitAnswer(ChildPreviousNamePage(Index(0), Index(0)), childName),
        submitAnswer(AddChildPreviousNamePage(Index(0)), true),
        submitAnswer(ChildPreviousNamePage(Index(0), Index(1)), childName),
        goTo(RemoveChildPreviousNamePage(Index(0), Index(1))),
        removeAddToListItem(ChildPreviousNamePage(Index(0), Index(1))),
        pageMustBe(AddChildPreviousNamePage(Index(0))),
        goTo(RemoveChildPreviousNamePage(Index(0), Index(0))),
        removeAddToListItem(ChildPreviousNamePage(Index(0), Index(0))),
        pageMustBe(ChildHasPreviousNamePage(Index(0))),
        answersMustNotContain(ChildNameChangedByDeedPollPage(Index(0)))
      )
  }

  "users whose child was registered in England" - {

    "where their birth certificate has a system number" - {

      "must be asked for the birth certificate system number" in {

        val relationship = arbitrary[ApplicantRelationshipToChild].sample.value

        startingFrom(ChildBirthRegistrationCountryPage(Index(0)))
          .run(
            submitAnswer(ChildBirthRegistrationCountryPage(Index(0)), England),
            submitAnswer(BirthCertificateHasSystemNumberPage(Index(0)), true),
            submitAnswer(ChildBirthCertificateSystemNumberPage(Index(0)), "123456789"),
            submitAnswer(ApplicantRelationshipToChildPage(Index(0)), relationship),
            submitAnswer(AdoptingThroughLocalAuthorityPage(Index(0)), false),
            submitAnswer(AnyoneClaimedForChildBeforePage(Index(0)), false),
            pageMustBe(CheckChildDetailsPage(Index(0)))
          )
      }
    }

    "where their birth certificate does not have a system number" - {

      "must not be asked for the birth certificate system number" in {

        val relationship = arbitrary[ApplicantRelationshipToChild].sample.value

        startingFrom(ChildBirthRegistrationCountryPage(Index(0)))
          .run(
            submitAnswer(ChildBirthRegistrationCountryPage(Index(0)), England),
            submitAnswer(BirthCertificateHasSystemNumberPage(Index(0)), false),
            submitAnswer(ApplicantRelationshipToChildPage(Index(0)), relationship),
            submitAnswer(AdoptingThroughLocalAuthorityPage(Index(0)), false),
            submitAnswer(AnyoneClaimedForChildBeforePage(Index(0)), false),
            pageMustBe(CheckChildDetailsPage(Index(0)))
          )
      }
    }
  }

  "users whose child was registered in Wales" - {

    "where their birth certificate has a system number" - {

      "must be asked for the birth certificate system number" in {

        val relationship = arbitrary[ApplicantRelationshipToChild].sample.value

        startingFrom(ChildBirthRegistrationCountryPage(Index(0)))
          .run(
            submitAnswer(ChildBirthRegistrationCountryPage(Index(0)), Wales),
            submitAnswer(BirthCertificateHasSystemNumberPage(Index(0)), true),
            submitAnswer(ChildBirthCertificateSystemNumberPage(Index(0)), "123456789"),
            submitAnswer(ApplicantRelationshipToChildPage(Index(0)), relationship),
            submitAnswer(AdoptingThroughLocalAuthorityPage(Index(0)), false),
            submitAnswer(AnyoneClaimedForChildBeforePage(Index(0)), false),
            pageMustBe(CheckChildDetailsPage(Index(0)))
          )
      }
    }

    "where their birth certificate does not have a system number" - {

      "must not be asked for the birth certificate system number" in {

        val relationship = arbitrary[ApplicantRelationshipToChild].sample.value

        startingFrom(ChildBirthRegistrationCountryPage(Index(0)))
          .run(
            submitAnswer(ChildBirthRegistrationCountryPage(Index(0)), Wales),
            submitAnswer(BirthCertificateHasSystemNumberPage(Index(0)), false),
            submitAnswer(ApplicantRelationshipToChildPage(Index(0)), relationship),
            submitAnswer(AdoptingThroughLocalAuthorityPage(Index(0)), false),
            submitAnswer(AnyoneClaimedForChildBeforePage(Index(0)), false),
            pageMustBe(CheckChildDetailsPage(Index(0)))
          )
      }
    }
  }

  "users whose child was registered in Scotland" - {

    "where their birth certificate has a system number" - {

      "must be asked for the birth certificate system number" in {

        val relationship = arbitrary[ApplicantRelationshipToChild].sample.value

        startingFrom(ChildBirthRegistrationCountryPage(Index(0)))
          .run(
            submitAnswer(ChildBirthRegistrationCountryPage(Index(0)), Scotland),
            submitAnswer(ScottishBirthCertificateHasNumbersPage(Index(0)), true),
            submitAnswer(ChildScottishBirthCertificateDetailsPage(Index(0)), "1234567890"),
            submitAnswer(ApplicantRelationshipToChildPage(Index(0)), relationship),
            submitAnswer(AdoptingThroughLocalAuthorityPage(Index(0)), false),
            submitAnswer(AnyoneClaimedForChildBeforePage(Index(0)), false),
            pageMustBe(CheckChildDetailsPage(Index(0)))
          )
      }
    }

    "where their birth certificate does not have a system number" - {

      "must not be asked for the birth certificate system number" in {

        val relationship = arbitrary[ApplicantRelationshipToChild].sample.value

        startingFrom(ChildBirthRegistrationCountryPage(Index(0)))
          .run(
            submitAnswer(ChildBirthRegistrationCountryPage(Index(0)), Scotland),
            submitAnswer(ScottishBirthCertificateHasNumbersPage(Index(0)), false),
            submitAnswer(ApplicantRelationshipToChildPage(Index(0)), relationship),
            submitAnswer(AdoptingThroughLocalAuthorityPage(Index(0)), false),
            submitAnswer(AnyoneClaimedForChildBeforePage(Index(0)), false),
            pageMustBe(CheckChildDetailsPage(Index(0)))
          )
      }
    }
  }

  "users whose child was registered in Northern Ireland" - {

    "must not be asked for birth certificate details" in {

      val relationship = arbitrary[ApplicantRelationshipToChild].sample.value

      startingFrom(ChildBirthRegistrationCountryPage(Index(0)))
        .run(
          submitAnswer(ChildBirthRegistrationCountryPage(Index(0)), NorthernIreland),
          submitAnswer(ApplicantRelationshipToChildPage(Index(0)), relationship),
          submitAnswer(AdoptingThroughLocalAuthorityPage(Index(0)), false),
          submitAnswer(AnyoneClaimedForChildBeforePage(Index(0)), false),
          pageMustBe(CheckChildDetailsPage(Index(0)))
        )
    }
  }

  "users whose child was registered outside of the UK" - {

    "must not be asked for birth certificate details" in {

      val relationship = arbitrary[ApplicantRelationshipToChild].sample.value

      startingFrom(ChildBirthRegistrationCountryPage(Index(0)))
        .run(
          submitAnswer(ChildBirthRegistrationCountryPage(Index(0)), Other),
          submitAnswer(ApplicantRelationshipToChildPage(Index(0)), relationship),
          submitAnswer(AdoptingThroughLocalAuthorityPage(Index(0)), false),
          submitAnswer(AnyoneClaimedForChildBeforePage(Index(0)), false),
          pageMustBe(CheckChildDetailsPage(Index(0)))
        )
    }
  }

  "users whose child's country of registration is unknown" - {

    "must not be asked for birth certificate details" in {

      val relationship = arbitrary[ApplicantRelationshipToChild].sample.value

      startingFrom(ChildBirthRegistrationCountryPage(Index(0)))
        .run(
          submitAnswer(ChildBirthRegistrationCountryPage(Index(0)), Unknown),
          submitAnswer(ApplicantRelationshipToChildPage(Index(0)), relationship),
          submitAnswer(AdoptingThroughLocalAuthorityPage(Index(0)), false),
          submitAnswer(AnyoneClaimedForChildBeforePage(Index(0)), false),
          pageMustBe(CheckChildDetailsPage(Index(0)))
        )
    }
  }

  "users whose child has been claimed for before" - {

    "must be asked for details of the previous claimant" in {

      val country      = arbitrary[ChildBirthRegistrationCountry].sample.value
      val claimantName = AdultName("first", None, "last")
      val claimantAddress = Address("line 1", None, "town", None, "postcode")

      val initialise = journeyOf(
        setUserAnswerTo(ChildBirthRegistrationCountryPage(Index(0)), country)
      )

      startingFrom(AnyoneClaimedForChildBeforePage(Index(0)))
        .run(
          initialise,
          submitAnswer(AnyoneClaimedForChildBeforePage(Index(0)), true),
          submitAnswer(PreviousClaimantNamePage(Index(0)), claimantName),
          submitAnswer(PreviousClaimantAddressPage(Index(0)), claimantAddress),
          pageMustBe(CheckChildDetailsPage(Index(0)))
        )
    }
  }
}