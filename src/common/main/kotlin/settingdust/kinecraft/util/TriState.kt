/*
* Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package settingdust.kinecraft.util

import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction
import java.util.*
import java.util.function.BooleanSupplier
import java.util.function.Supplier

/**
 * Represents a boolean value which can be true, false or refer to a default value.
 */
enum class TriState {
    /**
     * Represents the boolean value of `false`.
     */
    FALSE,

    /**
     * Represents a value that refers to a "default" value, often as a fallback.
     */
    DEFAULT,

    /**
     * Represents the boolean value of `true`.
     */
    TRUE;

    /**
     * Gets the value of the tri-state.
     *
     * @return true if the tri-state is [TriState.TRUE],
     * otherwise false.
     */
    fun get(): Boolean {
        return this == TRUE
    }

    val boxed: Boolean?
        /**
         * Gets the value of the tri-state as a boxed, nullable boolean.
         *
         * @return `null` if [TriState.DEFAULT].
         * Otherwise `true` if [TriState.TRUE] or `false` if [TriState.FALSE].
         */
        get() = if (this == DEFAULT) null else this.get()

    /**
     * Gets the value of this tri-state.
     * If the value is [TriState.DEFAULT] then use the supplied value.
     *
     * @param value the value to fall back to
     * @return the value of the tri-state or the supplied value if [TriState.DEFAULT].
     */
    fun orElse(value: Boolean): Boolean {
        return if (this == DEFAULT) value else this.get()
    }

    /**
     * Gets the value of this tri-state.
     * If the value is [TriState.DEFAULT] then use the supplied value.
     *
     * @param supplier the supplier used to get the value to fall back to
     * @return the value of the tri-state or the value of the supplier if the tri-state is [TriState.DEFAULT].
     */
    fun orElseGet(supplier: BooleanSupplier): Boolean {
        return if (this == DEFAULT) supplier.asBoolean else this.get()
    }

    /**
     * Maps the boolean value of this tri-state if it is [TriState.TRUE] or [TriState.FALSE].
     *
     * @param mapper the mapper to use
     * @param <T> the type of object being supplier by the mapper
     * @return an optional containing the mapped value; [Optional.empty] if the tri-state is [TriState.DEFAULT] or the value provided by the mapper is `null`.
    </T> */
    fun <T> map(mapper: Boolean2ObjectFunction<out T>): Optional<out T> {
        Objects.requireNonNull(mapper, "Mapper function cannot be null")

        if (this == DEFAULT) {
            return Optional.empty()
        }

        return Optional.ofNullable(mapper.apply(this.get()))
    }

    /**
     * Gets the value of this tri-state, or throws an exception if this tri-state's value is [TriState.DEFAULT].
     *
     * @param exceptionSupplier the supplying function that produces an exception to be thrown
     * @param <X> Type of the exception to be thrown
     * @return the value
     * @throws X if the value is [TriState.DEFAULT]
    </X> */
    fun orElseThrow(exceptionSupplier: Supplier<out Throwable>): Boolean {
        if (this != DEFAULT) {
            return this.get()
        }

        throw exceptionSupplier.get()
    }

    companion object {
        /**
         * Gets the corresponding tri-state from a boolean value.
         *
         * @param bool the boolean value
         * @return [TriState.TRUE] or [TriState.FALSE] depending on the value of the boolean.
         */
        fun of(bool: Boolean): TriState {
            return if (bool) TRUE else FALSE
        }

        /**
         * Gets a tri-state from a nullable boxed boolean.
         *
         * @param bool the boolean value
         * @return [TriState.DEFAULT] if `null`.
         * Otherwise [TriState.TRUE] or [TriState.FALSE] depending on the value of the boolean.
         */
        fun of(bool: Boolean?): TriState {
            return if (bool == null) DEFAULT else of(bool)
        }
    }
}