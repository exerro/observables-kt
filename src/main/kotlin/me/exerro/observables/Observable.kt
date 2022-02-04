package me.exerro.observables

/** An [Observable] is something that can have a callback attached, typically
 *  used to handle events in some sense. It is polymorphic over the function
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
 *  @see ObservableSignal
 *  @see ObservableStream
 *  @see MutableObservableValue
 *  */
fun interface Observable<in F> {
    /** Connect a callback to this [Observable] instance. */
    fun connect(callback: F): ObservableConnection

    /** @see Observable */
    companion object
}
