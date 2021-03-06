/*
 * Copyright 2013 - 2017 Outworkers Ltd.
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
package com.outworkers.phantom.jdk8

import java.time.{Instant, OffsetDateTime, ZoneId, ZonedDateTime}
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import com.outworkers.phantom.jdk8.dsl._
import scala.collection.JavaConverters._

class Jdk8TimeuuidConvertTests extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  private[this] val genLower: Int = -100000
  private[this] val genHigher: Int = -genLower

  implicit override val generatorDrivenConfig: PropertyCheckConfiguration = {
    PropertyCheckConfiguration(minSuccessful = 300)
  }

  val zoneIdGen: Gen[ZoneId] = Gen.oneOf(ZoneId.getAvailableZoneIds.asScala.toSeq) map ZoneId.of

  val zonedDateTimeGen: Gen[ZonedDateTime] = for {
    offset <- Gen.choose(genLower, genHigher)
    time = Instant.now().toEpochMilli
    dt = time + offset
    zone <- zoneIdGen
  } yield ZonedDateTime.ofInstant(Instant.ofEpochMilli(dt), zone)

  val offsetDateTimeGen: Gen[OffsetDateTime] = for {
    offset <- Gen.choose(genLower, genHigher)
    time = Instant.now().toEpochMilli
    dt = time + offset
    zone <- zoneIdGen
  } yield OffsetDateTime.ofInstant(Instant.ofEpochMilli(dt), zone)

  it should "convert a ZonedDateTime to a Timeuuid and back using the ZoneID argument method" in {
    forAll(zonedDateTimeGen) { dt =>
      dt.timeuuid.zonedDateTime(dt.getZone) shouldEqual dt
    }
  }

  it should "convert a ZonedDateTime to a Timeuuid and back using the string argument method" in {
    forAll(zonedDateTimeGen) { dt =>
      dt.timeuuid.zonedDateTime(dt.getZone.getId) shouldEqual dt
    }
  }

  it should "convert a OffsetDateTime to a Timeuuid and back using the string method" in {
    forAll(offsetDateTimeGen) { dt =>
      dt.timeuuid.offsetDateTime(dt.getOffset.getId) shouldEqual dt
    }
  }

  it should "convert a OffsetDateTime to a Timeuuid and back using the ZoneOffset overload method" in {
    forAll(offsetDateTimeGen) { dt =>
      dt.timeuuid.offsetDateTime(dt.getOffset) shouldEqual dt
    }
  }

}
