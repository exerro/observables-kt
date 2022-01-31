package me.exerro.observables

/** Test that a callback connected to a unit signal is invoked when the signal
 *  is emitted. */
fun testUnitSignalCallbackCalled() {
    val (s1, emitS1) = Observable.createUnitSignal()
    var called = false

    s1.connect { called = true }
    emitS1()

    assert(called) { "Signal callback was not invoked." }
    println("Signal callback invoked.")
}

/** Test that a callback connected to a unit signal that is then disconnected
 *  will not be invoked when the signal is emitted. */
fun testUnitSignalDisconnect() {
    val (s1, emitS1) = Observable.createUnitSignal()
    var called1 = false

    fun fn1() {
        called1 = true
    }

    val c1 = s1.connect(::fn1)
    c1.disconnect()
    emitS1()

    assert(!called1) { "Signal callback was invoked." }

    println("Signal callbacks disconnected successfully.")
}

/** Test that multiple connections may be made to a unit signal. */
fun testMultipleConnections() {
    val (s1, emitS1) = Observable.createUnitSignal()
    var called1 = false
    var called2 = false

    s1.connect { called1 = true }
    s1.connect { called2 = true }
    emitS1()

    assert(called1) { "First callback was not invoked." }
    assert(called2) { "Second callback was not invoked." }

    println("Both callbacks were invoked.")
}

/** Test that the values given to the connected callbacks of a signal are
 *  correct. */
fun testSignalValues() {
    val (s1, emitS1) = Observable.createSignal<Int>()
    val (s2, emitS2) = Observable.createBiSignal<Float, Float>()
    var acc1 = 0
    var acc2 = 0f

    s1.connect { acc1 += it }
    s2.connect { a, b -> acc2 += a - b }
    emitS1(3)
    emitS1(5)
    emitS2(5f, 2f)
    emitS2(4f, 6f)

    assert(acc1 == 8) { "Signal 1 callback was invoked with incorrect values." }
    assert(acc2 == 1f) { "Signal 2 callback was invoked with incorrect values." }

    println("Signal callbacks invoked with correct values.")
}

fun testSignalsOf() {
    fun randomCount() = 4 + (Math.random() * 5).toInt()

    val c1 = randomCount()
    val i2 = (1 .. randomCount()).map { 1 + (Math.random() * 10).toInt() }
    val i3a = (1 .. randomCount()).map { 1 + (Math.random() * 10).toInt() }
    val i3b = (1 .. randomCount()).map { 1 + (Math.random() * 10).toInt() }

    var r1 = 0
    var r2 = 0
    var r3a = 0
    var r3b = 0
    var r4 = 0
    var r5 = 0

    Observable.createUnitSignals(c1).connect { ++r1 }
    Observable.createSignalOf(i2).connect { r2 += it }
    Observable.createBiSignalOf(i3a zip i3b).connect { a, b ->
        r3a += a
        r3b += b
    }
    Observable.createSignalOf(1, 2, 3).connect { r4 += it }
    Observable.createBiSignalOf(1 to 2, 3 to 4).connect { a, b -> r5 += a + b }

    assert(r1 == c1) { "Unit signal called incorrect number of times" }
    assert(r2 == i2.sum()) { "Signal called with wrong values" }
    assert(r3a == i3a.sum()) { "Binary signal called with wrong values" }
    assert(r3b == i3b.sum()) { "Binary signal called with wrong values" }
    assert(r4 == 6) { "Vararg signal called with wrong values" }
    assert(r5 == 10) { "Vararg signal called with wrong values" }

    println("create*SignalOf ok")
}

fun main() {
    testUnitSignalCallbackCalled()
    testUnitSignalDisconnect()
    testMultipleConnections()
    testSignalValues()
    testSignalsOf()
}
