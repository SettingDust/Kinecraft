/*
 * Copyright 2023 The Quilt Project
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

@file:OptIn(ExperimentalSerializationApi::class)

package org.quiltmc.qkl.library.serialization.internal.decoder

import com.mojang.serialization.MapLike
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import org.quiltmc.qkl.library.serialization.internal.ElementOptions
import org.quiltmc.qkl.library.serialization.internal.SerializationConfig
import org.quiltmc.qkl.library.serialization.internal.util.collectInvalidKeys
import settingdust.kinecraft.serialization.unwrap

internal sealed class CollectionState<T : Any>(serializationConfig: SerializationConfig<T>) :
    DecoderState<T>(serializationConfig) {
    override val isStructure = true

    protected var nextIndex = -1

    abstract override val collectionSize: Int

    override fun getNextIndex(descriptor: SerialDescriptor): Int {
        if (nextIndex < collectionSize - 1) {
            nextIndex++
            return nextIndex
        }
        return CompositeDecoder.DECODE_DONE
    }
}

internal class ListState<T : Any>(
    private val input: List<T>,
    serializationConfig: SerializationConfig<T>
) : CollectionState<T>(serializationConfig) {
    override val collectionSize = input.size

    override fun getElement(): Pair<T, ElementOptions> {
        return input[++nextIndex] to ElementOptions()
    }

    override fun getElementTrace(): String {
        return "[$nextIndex]"
    }
}

internal class RegularMapState<T : Any>(
    mapLike: MapLike<T>,
    serializationConfig: SerializationConfig<T>
) : CollectionState<T>(serializationConfig) {
    private val entries = mapLike.entries().toList()

    override val collectionSize: Int = entries.size * 2

    override fun decodeSequentially() = false

    override fun getElement(): Pair<T, ElementOptions> {
        val index = nextIndex / 2
        return if (nextIndex % 2 == 0) {
            entries[index].first to ElementOptions(isMapKey = true)
        } else {
            entries[index].second to ElementOptions()
        }
    }

    override fun getElementTrace(): String {
        val entry = entries[nextIndex / 2]

        return if (nextIndex % 2 == 0) {
            val string = ops.getStringValue(entry.first).result().orElse(entry.toString())
            ".$string (key)"
        } else {
            val string = ops.getStringValue(entry.second).result().orElse(entry.toString())
            ".$string"
        }
    }
}

internal class EntryListMapState<T : Any>(
    entryList: List<T>,
    serializationConfig: SerializationConfig<T>
) : CollectionState<T>(serializationConfig) {
    private var parseIndex = -1
    private val parsedEntries =
        entryList.flatMapIndexed { index, entry ->
            parseIndex = index

            val map = ops.getMap(entry).unwrap()

            // if fields are invalid, but count() <= 2, key or value is missing, which crashes below
            if (map.entries().count() > 2 && !options.ignoreUnknownKeys) {
                val invalidKeys = collectInvalidKeys(map, ops, setOf("key", "value"))

                throw IllegalArgumentException(
                    "Map entry contains unknown fields: ${invalidKeys.joinToString { "'$it'" }}"
                )
            }

            val key = map["key"]
            val value = map["value"]

            if (key != null && value != null) {
                return@flatMapIndexed listOf(key, value)
            }

            val missingFields = buildList {
                if (key == null) {
                    add("key")
                }

                if (value == null) {
                    add("value")
                }
            }

            throw MissingFieldException(
                missingFields,
                "Map entry is missing required fields: $missingFields"
            )
        }

    override val collectionSize = entryList.size

    override fun getElement(): Pair<T, ElementOptions> {
        return parsedEntries[++nextIndex] to ElementOptions()
    }

    override fun getElementTrace(): String {
        return when {
            nextIndex < 0 -> ".$parseIndex"
            nextIndex % 2 == 0 -> ".${nextIndex / 2}.key"
            else -> ".${nextIndex / 2}.value"
        }
    }
}
