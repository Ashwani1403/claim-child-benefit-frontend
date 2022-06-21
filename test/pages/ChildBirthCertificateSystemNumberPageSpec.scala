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
import models.Index
import pages.behaviours.PageBehaviours


class ChildBirthCertificateSystemNumberPageSpec extends PageBehaviours {

  "ChildBirthCertificateSystemNumberPage" - {

    beRetrievable[String](ChildBirthCertificateSystemNumberPage(index))

    beSettable[String](ChildBirthCertificateSystemNumberPage(index))

    beRemovable[String](ChildBirthCertificateSystemNumberPage(index))

    "must navigate" - {

      "when there are no waypoints" - {

        val waypoints = EmptyWaypoints

        "to Applicant Relationship to Child for the same index" in {

          ChildBirthCertificateSystemNumberPage(Index(0))
            .navigate(waypoints, emptyUserAnswers)
            .mustEqual(routes.ApplicantRelationshipToChildController.onPageLoad(waypoints, Index(0)))

          ChildBirthCertificateSystemNumberPage(Index(1))
            .navigate(waypoints, emptyUserAnswers)
            .mustEqual(routes.ApplicantRelationshipToChildController.onPageLoad(waypoints, Index(1)))
        }
      }
    }
  }
}
