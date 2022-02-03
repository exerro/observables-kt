package me.exerro.observables

import kotlin.reflect.KProperty

/** An [MutableObservableValue] is a wrapper around a mutable value, which can
 *  be connected to, to get notified of changes. It's the mutable equivalent of
 *  [ObservableValue], letting you directly set the value.
 *
 *  @see create
 *  @See createLateInit */
interface MutableObservableValue<T>: ObservableValue<T> {
    override var currentValue: T

    /** Implement [setValue] to make this a valid mutable property delegate. */
    operator fun setValue(self: Any?, property: KProperty<*>, value: T)

    companion object {
        /** Create a [MutableObservableValue] with an [initialValue]. */
        fun <T> create(
            initialValue: T
        ) = object: MutableObservableValue<T> {
            override var currentValue = initialValue
                set(value) {
                    field = value
                    manager.forEach { it(value) }
                }

            override val isInitialised = true

            override fun setValue(self: Any?, property: KProperty<*>, value: T) {
                currentValue = value
            }

            override fun getValue(self: Any?, property: KProperty<*>): T =
                currentValue

            override fun connect(onChanged: (T) -> Unit) =
                manager.add(onChanged)

            private val manager = InternalConnectionManager<(T) -> Unit>()
        }

        /** Create a [MutableObservableValue] to be initialised later. */
        fun <T: Any> createLateInit() = object: MutableObservableValue<T> {
            override var currentValue: T
                get() = value
                set(value) {
                    this.value = value
                    manager.forEach { it(value) }
                }

            override val isInitialised = ::value.isInitialized

            override fun setValue(self: Any?, property: KProperty<*>, value: T) {
                currentValue = value
            }

            override fun getValue(self: Any?, property: KProperty<*>): T =
                currentValue

            override fun connect(onChanged: (T) -> Unit) =
                manager.add(onChanged)

            private lateinit var value: T
            private val manager = InternalConnectionManager<(T) -> Unit>()
        }
    }
}
