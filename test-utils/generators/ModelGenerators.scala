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

package generators

import models._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import uk.gov.hmrc.domain.Nino

trait ModelGenerators {

  implicit lazy val arbitraryPaymentFrequency: Arbitrary[PaymentFrequency] =
    Arbitrary {
      Gen.oneOf(PaymentFrequency.values)
    }

  implicit lazy val arbitraryChildName: Arbitrary[ChildName] =
    Arbitrary {
      for {
        firstName <- arbitrary[String]
        middleNames <- Gen.option(arbitrary[String])
        lastName <- arbitrary[String]
      } yield ChildName(firstName, middleNames, lastName)
    }

  implicit lazy val arbitraryChildBirthRegistrationCountry: Arbitrary[ChildBirthRegistrationCountry] =
    Arbitrary {
      Gen.oneOf(ChildBirthRegistrationCountry.values)
    }

  implicit lazy val arbitraryChildBiologicalSex: Arbitrary[ChildBiologicalSex] =
    Arbitrary {
      Gen.oneOf(ChildBiologicalSex.values)
    }

  implicit lazy val arbitraryApplicantRelationshipToChild: Arbitrary[ApplicantRelationshipToChild] =
    Arbitrary {
      Gen.oneOf(ApplicantRelationshipToChild.values)
    }

  implicit lazy val arbitraryBestTimeToContact: Arbitrary[BestTimeToContact] = {
    Arbitrary {
      Gen.oneOf(BestTimeToContact.values)
    }
  }

  implicit lazy val arbitraryNino: Arbitrary[Nino] = Arbitrary {
    for {
      firstChar <- Gen.oneOf('A', 'C', 'E', 'H', 'J', 'L', 'M', 'O', 'P', 'R', 'S', 'W', 'X', 'Y').map(_.toString)
      secondChar <- Gen.oneOf('A', 'B', 'C', 'E', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'R', 'S', 'T', 'W', 'X', 'Y', 'Z').map(_.toString)
      digits <- Gen.listOfN(6, Gen.numChar)
      lastChar <- Gen.oneOf('A', 'B', 'C', 'D')
    } yield Nino(firstChar ++ secondChar ++ digits :+ lastChar)
  }

  implicit lazy val arbitraryApplicantName: Arbitrary[AdultName] =
    Arbitrary {
      for {
        firstName   <- arbitrary[String]
        middleNames <- Gen.option(arbitrary[String])
        lastName    <- arbitrary[String]
      } yield AdultName(firstName, middleNames, lastName)
    }

  implicit lazy val arbitraryApplicantEmploymentStatus: Arbitrary[EmploymentStatus] =
    Arbitrary {
      Gen.oneOf(EmploymentStatus.values)
    }

  implicit lazy val arbitraryAddress: Arbitrary[Address] =
    Arbitrary {
      for {
        line1    <- arbitrary[String]
        line2    <- Gen.option(arbitrary[String])
        town     <- arbitrary[String]
        county   <- Gen.option(arbitrary[String])
        postcode <- arbitrary[String]
      } yield Address(line1, line2, town, county, postcode)
    }

  implicit lazy val arbitraryBankAccountDetails: Arbitrary[BankAccountDetails] =
    Arbitrary {
      for {
        accountName <- arbitrary[String]
        accountNumber <- arbitrary[String]
        sortCode <- arbitrary[String]
        rollNumber <- Gen.option(arbitrary[String])
      } yield BankAccountDetails(accountName, accountNumber, sortCode, rollNumber)
    }

  implicit lazy val arbitraryBenefits: Arbitrary[Benefits] =
    Arbitrary {
      Gen.oneOf(Benefits.values)
    }

  implicit lazy val arbitraryRelationshipStatus: Arbitrary[RelationshipStatus] =
    Arbitrary {
      Gen.oneOf(RelationshipStatus.values)
    }
}
