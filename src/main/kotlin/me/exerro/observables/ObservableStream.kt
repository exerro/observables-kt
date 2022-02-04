package me.exerro.observables

/** A stream of values, which can be connected to.
 *
 *  ```
 *  // example usage
 *  val items = ObservableStream.of(2, -1)
 *      .map { it + 1 }
 *      .filter { it > 0 }
 *      .flatMap { listOf(it, it) }
 *      .connect(::println)
 *  //> 3
 *  //> 3
 *  ```
 *  @see create
 *  @see of
 *  */
fun interface ObservableStream<out T>: Observable<(T) -> Unit> {
    /** Map the values of this stream using a [map] function. */
    fun <R> map(map: (T) -> R) = ObservableStream<R> { f ->
        connect { f(map(it)) }
    }

    /** Filter the values of this stream using a [predicate]. */
    fun filter(predicate: (T) -> Boolean) = ObservableStream<T> { f ->
        connect { if (predicate(it)) f(it) }
    }

    /** Map the values of this stream to an iterable using a [map] function,
     *  flattening the result into a standard [ObservableStream]. */
    fun <R> flatMap(map: (T) -> Iterable<R>) = ObservableStream<R> { f ->
        connect { for (item in map(it)) { f(item) } }
    }

    /** Fold values of this stream with an [initial] value using [onEach]. The
     *  resultant stream will emit successive accumulator values, including the
     *  [initial] one if [includeInitial] is true. */
    fun <A> fold(
        initial: A,
        includeInitial: Boolean = true,
        onEach: (A, T) -> A,
    ) = ObservableStream<A> { f ->
        var current = initial
        val lock = Object()

        if (includeInitial) f(initial)

        connect {
            val new = synchronized(lock) {
                current = onEach(current, it)
                current
            }
            f(new)
        }
    }

    /** @see ObservableStream */
    companion object {
        /** Create a pair of an [ObservableStream] and a function that pushes
         *  values to it.
         *
         *  Example usage:
         *  ```
         *  val (stream, push) = ObservableStream.create<Int>()
         *
         *  stream.connect(::println)
         *  push(27)
         *  //> 27
         *  ``` */
        fun <T> create(): Pair<ObservableStream<T>, (T) -> Unit> {
            val connections = ConnectionHelper<(T) -> Unit>()
            val stream = ObservableStream(connections::add)
            return stream to { value -> connections.forEach { it(value) } }
        }

        /** Create an [ObservableStream] which, when connected to, invokes the
         *  callback with each item in [items].
         *
         *  Example usage:
         *  ```
         *  val stream = ObservableStream.of(listOf(1, 2, 3))
         *
         *  stream.connect(::println)
         *  //> 1
         *  //> 2
         *  //> 3
         *  ``` */
        fun <T> of(items: Iterable<T>) = ObservableStream<T> {
            items.forEach(it)
            ObservableConnection.blank
        }

        /** Create an [ObservableStream] which, when connected to, invokes the
         *  callback with each item in [items].
         *
         *  Example usage:
         *  ```
         *  val stream = ObservableStream.of(1, 2, 3)
         *
         *  stream.connect(::println)
         *  //> 1
         *  //> 2
         *  //> 3
         *  ``` */
        fun <T> of(vararg items: T) = of(items.toList())
    }
}
