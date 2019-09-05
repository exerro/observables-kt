
/** Test that a callback connected to a unit signal is invoked when the signal
 *  is emitted. */
fun testUnitSignalCallbackCalled() {
    val s1 = UnitSignal()
    var called = false

    s1.connect { called = true }
    s1.emit()

    assert(called) { "Signal callback was not invoked." }
    println("Signal callback invoked.")
}

/** Test that a callback connected to a unit signal that is then disconnected
 *  will not be invoked when the signal is emitted. */
fun testUnitSignalDisconnect() {
    val s1 = UnitSignal()
    val s2 = UnitSignal()
    var called1 = false
    var called2 = false

    fun fn1() {
        called1 = true
    }

    s1.connect(::fn1)
    s2.connect { called2 = true }
    s1.disconnect(::fn1)
    s2.disconnectAll()
    s1.emit()
    s2.emit()

    assert(!called1) { "Signal 1 callback was invoked." }
    assert(!called2) { "Signal 2 callback was invoked." }

    println("Signal callbacks disconnected successfully.")
}

/** Test that multiple connections may be made to a unit signal. */
fun testMultipleConnections() {
    val s1 = UnitSignal()
    var called1 = false
    var called2 = false

    s1.connect { called1 = true }
    s1.connect { called2 = true }

    assert(called1) { "First callback was not invoked." }
    assert(called2) { "Second callback was not invoked." }

    println("Both callbacks were invoked.")
}

/** Test that the values given to the connected callbacks of a signal are
 *  correct. */
fun testSignalValues() {
    val s1 = Signal<Int>()
    val s2 = BiSignal<Float, Float>()
    var acc1 = 0
    var acc2 = 0f

    s1.connect { acc1 += it }
    s2.connect { a, b -> acc2 += a - b }
    s1.emit(3)
    s1.emit(5)
    s2.emit(5f, 2f)
    s2.emit(4f, 6f)

    assert(acc1 == 8) { "Signal 1 callback was invoked with incorrect values." }
    assert(acc2 == 1f) { "Signal 2 callback was invoked with incorrect values." }

    println("Signal callbacks invoked with correct values.")
}

fun testConnectionManager() {
    // TODO
}

fun testSignalTemporaryCallbacks() {
    // TODO
}

fun testSignalOnceCallbacks() {
    // TODO
}

fun main() {
    testUnitSignalCallbackCalled()
    testUnitSignalDisconnect()
    testMultipleConnections()
    testSignalValues()
    testConnectionManager()
    testSignalTemporaryCallbacks()
    testSignalOnceCallbacks()
}
