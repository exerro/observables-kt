package me.exerro.observables

/** A stream of values which can be connected to. */
fun interface ObservableStream<out T>: Observable<(T) -> Unit> {
    /** Map the values of this stream using a [map] function. */
    fun <R> map(map: (T) -> R) = ObservableStream { f ->
        connect { f(map(it)) }
    }

    /** Filter the values of this stream using a [predicate]. */
    fun filter(predicate: (T) -> Boolean) = ObservableStream { f ->
        connect { if (predicate(it)) f(it) }
    }

    /** Map the values of this stream to an iterable using a [map] function,
     *  flattening the result into a standard [ObservableStream]. */
    fun <R> flatMap(map: (T) -> Iterable<R>) = ObservableStream { f ->
        connect { for (item in map(it)) { f(item) } }
    }

    /** Fold values of this stream with an [initial] value using [onEach]. The
     *  resultant stream will emit successive accumulator values, including the
     *  [initial] one if [includeInitial] is true. */
    fun <A> fold(
        initial: A,
        includeInitial: Boolean = true,
        onEach: (A, T) -> A,
    ) = ObservableStream { f ->
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
}
