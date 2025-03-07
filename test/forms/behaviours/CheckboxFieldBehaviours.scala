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

package forms.behaviours

import forms.FormSpec
import models.WithName
import play.api.data.{Form, FormError}

trait CheckboxFieldBehaviours extends FormSpec {

  def checkboxField[T](form: Form[_],
                       fieldName: String,
                       validValues: Seq[T],
                       invalidError: FormError): Unit = {
    for {
      (value, i) <- validValues.zipWithIndex
    } yield s"binds `$value` successfully" in {
      val data = Map(
        s"$fieldName[$i]" -> value.toString
      )
      val result = form.bind(data)
      result.get mustEqual Set(value)
      result.errors mustBe empty
    }

    "fail to bind when the answer is invalid" in {
      val data = Map(
        s"$fieldName[0]" -> "invalid value"
      )
      form.bind(data).errors must contain(invalidError)
    }
  }

  def mandatoryCheckboxField(form: Form[_],
                             fieldName: String,
                             requiredKey: String,
                             args: Any*): Unit = {

    "fail to bind when no answers are selected" in {
      val data = Map.empty[String, String]
      form.bind(data).errors must contain(FormError(s"$fieldName", requiredKey, args))
    }

    "fail to bind when blank answer provided" in {
      val data = Map(
        s"$fieldName[0]" -> ""
      )
      form.bind(data).errors must contain(FormError(s"$fieldName[0]", requiredKey, args))
    }
  }

  def checkboxFieldWithMutuallyExclusiveAnswers[A](
                                                    form: Form[_],
                                                    fieldName: String,
                                                    set1: Set[A],
                                                    set2: Set[A],
                                                    expectedError: FormError
                                                  ): Unit = {

    "must bind when all values are from the first set" in {
      val data = set1.toList.zipWithIndex.map { case (item, index) =>
        s"$fieldName[$index]" -> item.toString
      }.toMap

      val result = form.bind(data)
      result.get mustEqual set1
      result.errors mustBe empty
    }

    "must bind when all values are from the second set" in {
      val data = set2.toList.zipWithIndex.map { case (item, index) =>
        s"$fieldName[$index]" -> item.toString
      }.toMap

      val result = form.bind(data)
      result.get mustEqual set2
      result.errors mustBe empty
    }

    "must not bind when there are values from both sets" in {
      val data = set1.union(set2).toList.zipWithIndex.map { case (item, index) =>
        s"$fieldName[$index]" -> item.toString
      }.toMap

      form.bind(data).errors must contain only expectedError
    }
  }
}
