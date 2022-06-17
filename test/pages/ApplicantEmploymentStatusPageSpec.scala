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

package pages

import controllers.routes
import models.ApplicantEmploymentStatus
import models.RelationshipStatus._
import pages.behaviours.PageBehaviours

class ApplicantEmploymentStatusPageSpec extends PageBehaviours {

  "ApplicantEmploymentStatusPage" - {

    beRetrievable[Set[ApplicantEmploymentStatus]](ApplicantEmploymentStatusPage)

    beSettable[Set[ApplicantEmploymentStatus]](ApplicantEmploymentStatusPage)

    beRemovable[Set[ApplicantEmploymentStatus]](ApplicantEmploymentStatusPage)

    "must navigate" - {

      "when there are no waypoints" - {

        val waypoints = EmptyWaypoints

        "to Index when the relationship status is Single" in {

          val answers = emptyUserAnswers.set(RelationshipStatusPage, Single).success.value

          ApplicantEmploymentStatusPage
            .navigate(waypoints, answers)
            .mustEqual(routes.IndexController.onPageLoad)
        }

        "to Index when the relationship status is Widowed" in {

          val answers = emptyUserAnswers.set(RelationshipStatusPage, Widowed).success.value

          ApplicantEmploymentStatusPage
            .navigate(waypoints, answers)
            .mustEqual(routes.IndexController.onPageLoad)
        }

        "to Index when the relationship status is Divorced" in {

          val answers = emptyUserAnswers.set(RelationshipStatusPage, Divorced).success.value

          ApplicantEmploymentStatusPage
            .navigate(waypoints, answers)
            .mustEqual(routes.IndexController.onPageLoad)
        }

        "to Index when the relationship status is Separated" in {

          val answers = emptyUserAnswers.set(RelationshipStatusPage, Separated).success.value

          ApplicantEmploymentStatusPage
            .navigate(waypoints, answers)
            .mustEqual(routes.IndexController.onPageLoad)
        }

        "to Partner Name when the relationship status is Married" in {

          val answers = emptyUserAnswers.set(RelationshipStatusPage, Married).success.value

          ApplicantEmploymentStatusPage
            .navigate(waypoints, answers)
            .mustEqual(routes.PartnerNameController.onPageLoad(waypoints))
        }

        "to Partner Name when the relationship status is Cohabiting" in {

          val answers = emptyUserAnswers.set(RelationshipStatusPage, Cohabiting).success.value

          ApplicantEmploymentStatusPage
            .navigate(waypoints, answers)
            .mustEqual(routes.PartnerNameController.onPageLoad(waypoints))
        }
      }
    }
  }
}
