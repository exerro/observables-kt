package me.exerro.observables

/** Test that [ObservableStream.create] returns a working stream and a function
 *  that pushes to it. */
fun testObservableStreamCreate() {
    var value = 0
    val (stream, push) = ObservableStream.create<Int>()

    stream.connect { value = it }
    push(3)

    assert(value == 3)
    println("ObservableStream callback was called")
}

/** Test that [ObservableStream.of] returns a stream which, when connected to,
 *  calls the callback with the initial items given. */
fun testObservableStreamOf() {
    val items = listOf(1, 2, 3)
    var counter = 0

    ObservableStream.of(items).connect { counter += it }

    assert(counter == 6)
    println("ObservableStream.of callback called with correct values")
}

/** Test that [ObservableStream.map] maps values for the resultant stream using
 *  the function given. */
fun testObservableStreamMap() {
    val items = listOf(1, 2, 3)
    var counter = 0

    ObservableStream.of(items).map { it + 1 }.connect { counter += it }

    assert(counter == 9)
    println("Mapped ObservableStream callback called with correct values")
}

/** Test that [ObservableStream.filter] filters values for the resultant stream
 *  using the function given. */
fun testObservableStreamFilter() {
    val items = listOf(1, 2, 3)
    var counter = 0

    ObservableStream.of(items).filter { it > 1 }.connect { counter += it }

    assert(counter == 5)
    println("Filtered ObservableStream callback called with correct values")
}

/** Test that [ObservableStream.filterIsInstance] filters values to the given
 *  type. */
fun testObservableStreamFilterIsInstance() {
    var counter = 0

    ObservableStream.of("a", 2, 5).filterIsInstance<Int>().connect { counter += it }

    assert(counter == 7)
    println("Type filtered ObservableStream callback called with correct values")
}

/** Test that [ObservableStream.flatMap] flat maps values for the resultant
 *  stream using the function given. */
fun testObservableStreamFlatMap() {
    val items = listOf(1, 2, 3)
    val result = mutableListOf<Int>()

    ObservableStream
        .of(items)
        .flatMap { v -> (1 .. v).map { v } }
        .connect { result += it }

    assert(result == listOf(1, 2, 2, 3, 3, 3))
    println("FlatMapped ObservableStream callback called with correct values")
}

/** Test that [ObservableStream.map] maps values for the resultant stream using
 *  the function given. */
fun testObservableStreamFold() {
    val items = listOf(1, 2, 3)
    var counter1 = 0
    var counter2 = 0

    ObservableStream
        .of(items)
        .fold(9, includeInitial = true) { a, b -> a + b }
        .connect { counter1 += it }

    ObservableStream
        .of(items)
        .fold(9, includeInitial = false) { a, b -> a + b }
        .connect { counter2 += it }

    assert(counter1 == 46)
    assert(counter2 == 37)
    println("Folded ObservableStream callback called with correct values")
}
