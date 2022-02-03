package me.exerro.observables

/** An [Observable] is something that can have a callback attached, typically
 *  used to handle events in some sense. It's polymorphic over the function
 *  type, meaning particular types of [Observable] can have different callback
 *  types.
 *
 *  ```
 *  // example usage
 *  val observable = someLibraryFunction()
 *  val connection = observable.connect { stuff -> do(stuff) }
 *  // then later...
 *  connection.disconnect()
 *  ```
 *
 *  @see createSignal
 *  @see createSignalOf
 *  @see createUnitSignal
 *  @see createUnitSignalOf
 *  @see createBiSignal
 *  @see createBiSignalOf */
fun interface Observable<in F> {
    /** Connect a callback to this [Observable] instance. */
    fun connect(callback: F): ObservableConnection

    companion object {
        /** Return a pair of an [Observable] and an emitter which will invoke
         *  connected callbacks to the returned [Observable]. */
        fun createUnitSignal(): Pair<Observable<() -> Unit>, () -> Unit> {
            val manager = InternalConnectionManager<() -> Unit>()
            val stream = Observable(manager::add)
            return stream to { manager.forEach { it() } }
        }

        /** Return a pair of an [Observable] and an emitter which will invoke
         *  connected callbacks to the returned [Observable]. */
        fun <T> createSignal(): Pair<ObservableStream<T>, (T) -> Unit> {
            val manager = InternalConnectionManager<(T) -> Unit>()
            val stream = ObservableStream(manager::add)
            return stream to { value -> manager.forEach { it(value) } }
        }

        /** Return a pair of an [Observable] and an emitter which will invoke
         *  connected callbacks to the returned [Observable]. */
        fun <A, B> createBiSignal(): Pair<Observable<(A, B) -> Unit>, (A, B) -> Unit> {
            val manager = InternalConnectionManager<(A, B) -> Unit>()
            val stream = Observable(manager::add)
            return stream to { a, b -> manager.forEach { it(a, b) } }
        }

        /** Return a an [Observable] which, when connected to, will invoke the
         *  callback [count] times. */
        fun createUnitSignalOf(count: Int): Observable<() -> Unit> =
            Observable {
                repeat(count) { it() }
                ObservableConnection.blank
            }

        /** Return a an [Observable] which, when connected to, will invoke the
         *  callback for each item in [items]. */
        fun <T> createSignalOf(items: Iterable<T>): ObservableStream<T> =
            ObservableStream {
                items.forEach(it)
                ObservableConnection.blank
            }

        /** Return a an [Observable] which, when connected to, will invoke the
         *  callback for each pair of items in [items]. */
        fun <A, B> createBiSignalOf(
            items: Iterable<Pair<A, B>>
        ): Observable<(A, B) -> Unit> = Observable { f ->
            items.forEach { f(it.first, it.second) }
            ObservableConnection.blank
        }

        /** Return a an [Observable] which, when connected to, will invoke the
         *  callback for each item in [items]. */
        fun <T> createSignalOf(vararg items: T) =
            createSignalOf(items.toList())

        /** Return a an [Observable] which, when connected to, will invoke the
         *  callback for each pair of items in [items]. */
        fun <A, B> createBiSignalOf(vararg items: Pair<A, B>) =
            createBiSignalOf(items.toList())
    }
}
