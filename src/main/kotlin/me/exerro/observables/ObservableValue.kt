package me.exerro.observables

import kotlin.reflect.KProperty

/** An [ObservableValue] is a wrapper around a mutable value, which can be
 *  connected to, to get notified of changes. */
interface ObservableValue<out T>: ObservableStream<T> {
    /** Value currently held by this [ObservableValue]. */
    val currentValue: T

    /** Implement [getValue] to make this a valid property
     *  delegate. */
    operator fun getValue(self: Any?, property: KProperty<*>): T

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun connect(onChanged: (T) -> Unit): ObservableConnection


}
