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

package audit

import com.google.inject.{Inject, Singleton}
import models.JourneyModel
import play.api.libs.json.JsValue
import play.api.{Configuration, Logging}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

import scala.concurrent.ExecutionContext

@Singleton
class AuditService @Inject() (connector: AuditConnector, configuration: Configuration)(implicit ec: ExecutionContext) extends Logging {

  private val downloadEventName            = configuration.get[String]("auditing.downloadEventName")
  private val validateBankDetailsEventName = configuration.get[String]("auditing.validateBankDetailsEventName")

  def auditDownload(model: JourneyModel)(implicit hc: HeaderCarrier): Unit = {
    val data = DownloadAuditEvent.from(model)
    connector.sendExplicitAudit(downloadEventName, data)
  }

  def auditValidateBankDetails(auditEvent: ValidateBankDetailsAuditEvent)(implicit hc: HeaderCarrier): Unit =
    connector.sendExplicitAudit(validateBankDetailsEventName, auditEvent)
}
