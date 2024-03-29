package me.exerro.observables

import kotlin.reflect.KProperty

/** A [MutableObservableValue] is a wrapper around a mutable value which can
 *  be connected to to get notified of changes. It is the mutable equivalent of
 *  [ObservableValue], letting you directly set the value.
 *
 *  ```
 *  // example usage
 *  val observableValue = someLibraryFunction()
 *  var value by observableValue
 *
 *  println(value)
 *  //> something
 *
 *  observableValue.connect(::println)
 *  value = "hello"
 *  //> hello
 *  ```
 *  @see of
 *  @See createLateInit */
interface MutableObservableValue<T>: ObservableValue<T> {
    override var currentValue: T

    /** Implement [setValue] to make this a valid mutable property delegate. */
    operator fun setValue(self: Any?, property: KProperty<*>, value: T)

    /** @see MutableObservableValue */
    companion object {
        /** Create a [MutableObservableValue] with an [initialValue].
         *
         *  ```
         *  val value = MutableObservableValue.of(3)
         *
         *  println(value.currentValue)
         *  //> 3
         *
         *  value.connect(::println)
         *  value.currentValue = 2
         *  //> 2
         *  ```
         *  @see createLateInit
         *  */
        fun <T> of(
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

            private val manager = ConnectionHelper<(T) -> Unit>()
        }

        /** Create a [MutableObservableValue] to be initialised later.
         *
         *  ```
         *  val value = MutableObservableValue.createLateInit<Int>()
         *
         *  value.connect(::println)
         *  value.currentValue = 4
         *  //> 4
         *  ```
         *  @see create
         *  */
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
            private val manager = ConnectionHelper<(T) -> Unit>()
        }
    }
}
