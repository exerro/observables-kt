package me.exerro.observables

/** A signal is an event-like form of [Observable]. [ObservableSignal] is an
 *  object with constructors for various kinds of signal.
 *
 *  @see createUnitSignal
 *  @see createSignal
 *  @see createBiSignal
 *  @see createUnitSignalOf
 *  @see createSignalOf
 *  @see createBiSignalOf */
object ObservableSignal {
    /** Return a pair of an [Observable] and an emitter, which will invoke
     *  connected callbacks to the returned [Observable].
     *
     *  ```
     *  // example usage
     *  val (signal, emit) = ObservableSignal.createUnitSignal()
     *
     *  signal.connect { println("Emitted") }
     *  emit()
     *  //> Emitted
     *  ```
     *  */
    fun createUnitSignal(): Pair<Observable<() -> Unit>, () -> Unit> {
        val manager = ConnectionHelper<() -> Unit>()
        val stream = Observable(manager::add)
        return stream to { manager.forEach { it() } }
    }

    /** Return a pair of an [Observable] and an emitter, which will invoke
     *  connected callbacks to the returned [Observable].
     *
     *  ```
     *  // example usage
     *  val (signal, emit) = ObservableSignal.createSignal<Int>()
     *
     *  signal.connect(::println)
     *  emit(2)
     *  //> 2
     *  ```
     *  */
    fun <T> createSignal(): Pair<ObservableStream<T>, (T) -> Unit> {
        val manager = ConnectionHelper<(T) -> Unit>()
        val stream = ObservableStream(manager::add)
        return stream to { value -> manager.forEach { it(value) } }
    }

    /** Return a pair of an [Observable] and an emitter, which will invoke
     *  connected callbacks to the returned [Observable].
     *
     *  ```
     *  // example usage
     *  val (signal, emit) = ObservableSignal.createBiSignal<Int, String>()
     *
     *  signal.connect { a, b -> println("$a $b") }
     *  emit(1, "hi")
     *  //> 1 hi
     *  ```
     *  */
    fun <A, B> createBiSignal(): Pair<Observable<(A, B) -> Unit>, (A, B) -> Unit> {
        val manager = ConnectionHelper<(A, B) -> Unit>()
        val stream = Observable(manager::add)
        return stream to { a, b -> manager.forEach { it(a, b) } }
    }

    /** Return a an [Observable] which, when connected to, will invoke the
     *  callback [count] times.
     *
     *  ```
     *  // example usage
     *  val signal = ObservableSignal.createUnitSignalOf(1)
     *
     *  signal.connect { println("Emitted") }
     *  //> Emitted
     *  ```
     *  */
    fun createUnitSignalOf(count: Int): Observable<() -> Unit> =
        Observable {
            repeat(count) { it() }
            ObservableConnection.blank
        }

    /** Return a an [Observable] which, when connected to, will invoke the
     *  callback for each item in [items].
     *
     *  ```
     *  // example usage
     *  val signal = ObservableSignal.createSignalOf(listOf(1, 2))
     *
     *  signal.connect(::println)
     *  //> 1
     *  //> 2
     *  ```
     *  */
    fun <T> createSignalOf(items: Iterable<T>): ObservableStream<T> =
        ObservableStream {
            items.forEach(it)
            ObservableConnection.blank
        }

    /** Return a an [Observable] which, when connected to, will invoke the
     *  callback for each pair of items in [items].
     *
     *  ```
     *  // example usage
     *  val signal = ObservableSignal.createBiSignalOf(listOf(1 to "hi", 2 to "hello"))
     *
     *  signal.connect { a, b -> println("$a $b") }
     *  //> 1 hi
     *  //> 2 hello
     *  ```
     *  */
    fun <A, B> createBiSignalOf(
        items: Iterable<Pair<A, B>>
    ): Observable<(A, B) -> Unit> = Observable { f ->
        items.forEach { f(it.first, it.second) }
        ObservableConnection.blank
    }

    /** Return a an [Observable] which, when connected to, will invoke the
     *  callback for each item in [items].
     *
     *  ```
     *  // example usage
     *  val signal = ObservableSignal.createSignalOf(1, 2)
     *
     *  signal.connect(::println)
     *  //> 1
     *  //> 2
     *  ```
     *  */
    fun <T> createSignalOf(vararg items: T) =
        createSignalOf(items.toList())

    /** Return a an [Observable] which, when connected to, will invoke the
     *  callback for each pair of items in [items].
     *
     *  ```
     *  // example usage
     *  val signal = ObservableSignal.createBiSignalOf(1 to "hi", 2 to "hello")
     *
     *  signal.connect { a, b -> println("$a $b") }
     *  //> 1 hi
     *  //> 2 hello
     *  ```
     *  */
    fun <A, B> createBiSignalOf(vararg items: Pair<A, B>) =
        createBiSignalOf(items.toList())
}