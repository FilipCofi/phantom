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
package com.outworkers.phantom.keys

import com.outworkers.phantom.column.AbstractColumn

private[phantom] trait Undroppable
private[phantom] trait Unmodifiable
private[phantom] trait Indexed

private[phantom] trait Key[KeyType <: Key[KeyType]] {
  self: AbstractColumn[_] =>
}

trait PrimaryKey extends Key[PrimaryKey] with Unmodifiable with Indexed with Undroppable {
  self: AbstractColumn[_] =>
  override val isPrimary = true
}

trait PartitionKey extends Key[PartitionKey] with Unmodifiable with Indexed with Undroppable {
  self: AbstractColumn[_] =>
  override val isPartitionKey = true
  override val isPrimary = true
}

/**
 * A trait mixable into Column definitions to allow storing them as keys.
 */
trait Index extends Indexed with Undroppable {
  self: AbstractColumn[_] =>

  override val isSecondaryKey = true
}

trait Keys {
  self : Index with AbstractColumn[_] =>

  override private[phantom] val isMapKeyIndex = true
}

trait Entries {
  self: Index with AbstractColumn[_] =>

  override private[phantom] val isMapEntryIndex = true
}