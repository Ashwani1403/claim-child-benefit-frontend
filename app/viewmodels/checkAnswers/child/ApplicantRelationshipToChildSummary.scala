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

package viewmodels.checkAnswers.child

import models.{Index, UserAnswers}
import pages.child.ApplicantRelationshipToChildPage
import pages.{CheckAnswersPage, Waypoints}
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object ApplicantRelationshipToChildSummary {

  def row(answers: UserAnswers, index: Index, waypoints: Waypoints, sourcePage: CheckAnswersPage)
         (implicit messages: Messages): Option[SummaryListRow] =
    answers.get(ApplicantRelationshipToChildPage(index)).map {
      answer =>

        SummaryListRowViewModel(
          key = "applicantRelationshipToChild.checkYourAnswersLabel",
          value = ValueViewModel(messages(s"applicantRelationshipToChild.$answer")),
          actions = Seq(
            ActionItemViewModel("site.change", ApplicantRelationshipToChildPage(index).changeLink(waypoints, sourcePage).url)
              .withVisuallyHiddenText(messages("applicantRelationshipToChild.change.hidden"))
          )
        )
    }
}
