/**
 * Copyright 2011-2014 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.http.config

import org.scalatest.{ FlatSpec, Matchers }

import io.gatling.core.config.GatlingConfiguration
import io.gatling.http.request.ExtraInfo

class HttpProtocolBuilderSpec extends FlatSpec with Matchers {

  GatlingConfiguration.setUpForTest()

  "http protocol configuration builder" should "support an optional extra info extractor" in {

    val expectedExtractor = (extraInfo: ExtraInfo) => Nil

    val builder = HttpProtocolBuilder.DefaultHttpProtocolBuilder
      .disableWarmUp
      .extraInfoExtractor(expectedExtractor)
    val config: HttpProtocol = builder.build

    config.responsePart.extraInfoExtractor.get shouldBe expectedExtractor
  }

  it should "be able to support a base URL" in {
    val url = "http://url"

    val builder = HttpProtocolBuilder.DefaultHttpProtocolBuilder
      .baseURL(url)
      .disableWarmUp

    val config: HttpProtocol = builder.build

    Seq(config.baseURL.get, config.baseURL.get, config.baseURL.get) shouldBe Seq(url, url, url)
  }

  it should "provide a Round-Robin strategy when multiple urls are provided" in {
    val url1 = "http://url1"
    val url2 = "http://url2"

    val builder = HttpProtocolBuilder.DefaultHttpProtocolBuilder
      .baseURLs(url1, url2)
      .disableWarmUp

    val config: HttpProtocol = builder.build

    Seq(config.baseURL.get, config.baseURL.get, config.baseURL.get) shouldBe Seq(url1, url2, url1)
  }

  it should "set a silent URI regex" in {
    val builder = HttpProtocolBuilder.DefaultHttpProtocolBuilder
      .silentURI(".*")

    val config: HttpProtocol = builder.build

    val actualPattern: String = config.requestPart.silentURI.get.toString()
    actualPattern.equals(".*") shouldBe true
  }
}
