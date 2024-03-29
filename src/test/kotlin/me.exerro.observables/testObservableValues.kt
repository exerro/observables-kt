package me.exerro.observables

/** Test that [ObservableValue.of] returns an [ObservableValue] which behaves
 *  appropriately. */
fun testObservableValueOf() {
    val valueObservable = ObservableValue.of(1)
    val value by valueObservable

    assert(valueObservable.isInitialised)
    assert(valueObservable.currentValue == 1)
    assert(value == 1)

    println("ObservableValue.of is constant and awesome")
}

/** Test that [MutableObservableValue.create] returns a [MutableObservableValue]
 *  which behaves appropriately. */
fun testObservableValueCreate() {
    val valueObservable = MutableObservableValue.of(0)
    var value by valueObservable
    var sum = 0

    valueObservable.connect { sum += it }

    assert(valueObservable.isInitialised)
    assert(valueObservable.currentValue == 0)
    assert(value == 0)

    valueObservable.currentValue = 1

    assert(valueObservable.isInitialised)
    assert(valueObservable.currentValue == 1)
    assert(value == 1)

    value = 2

    assert(valueObservable.isInitialised)
    assert(valueObservable.currentValue == 2)
    assert(value == 2)

    assert(sum == 3)

    println("MutableObservableValue.create responds to changes appropriately")
}

/** Test that [MutableObservableValue.createLateInit] returns a
 *  [MutableObservableValue] which behaves appropriately and is uninitialised
 *  initially. */
fun testObservableValueCreateLateInit() {
    val valueObservable = MutableObservableValue.createLateInit<Int>()
    var value by valueObservable
    var sum = 0

    assert(!valueObservable.isInitialised)

    try {
        println(valueObservable.currentValue)
        error("Uninitialised lateinit observable value didn't error on access")
    }
    catch (e: Throwable) {
        assert(e is UninitializedPropertyAccessException)
    }

    valueObservable.connect { sum += it }

    valueObservable.currentValue = 3

    assert(valueObservable.isInitialised)
    assert(valueObservable.currentValue == 3)
    assert(value == 3)

    value = 4

    assert(valueObservable.isInitialised)
    assert(valueObservable.currentValue == 4)
    assert(value == 4)

    assert(sum == 7)

    println("MutableObservableValue.createLateInit responds to changes appropriately")
}
