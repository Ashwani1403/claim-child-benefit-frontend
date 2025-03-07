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

package models

import play.api.libs.json.{Json, OWrites}

final case class ValidateBankDetailsRequest(account: Account)

object ValidateBankDetailsRequest {

  def from(bankAccountDetails: BankAccountDetails): ValidateBankDetailsRequest =
    ValidateBankDetailsRequest(
      Account(
        sortCode      = bankAccountDetails.sortCodeTrimmed,
        accountNumber = bankAccountDetails.accountNumberPadded
      )
    )

  implicit val writes: OWrites[ValidateBankDetailsRequest] = Json.writes
}

final case class Account(sortCode: String, accountNumber: String)

object Account {
  implicit val writes: OWrites[Account] = Json.writes
}
