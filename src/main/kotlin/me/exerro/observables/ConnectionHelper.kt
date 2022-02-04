package me.exerro.observables

/** Manages a list of connections of type [F] in a thread-safe manner.
 *
 *  ```
 *  // example usage
 *  val helper = ConnectionHelper<() -> Unit>()
 *  val connection = helper.add { println("Here") }
 *
 *  helper.onEach { it() }
 *  //> Here
 *  connection.disconnect()
 *  helper.onEach { it() }
 *  ```
 *  */
class ConnectionHelper<F> {
    /** Add a callback, returning an [ObservableConnection] which will later
     *  remove it. */
    fun add(f: F): ObservableConnection {
        synchronized(connections) { connections.add(f) }
        return ObservableConnection {
            synchronized(connections) { connections.remove(f) }
        }
    }

    /** Run [F] with each callback. */
    fun forEach(fn: (F) -> Unit) {
        for (f in synchronized(connections) { connections.toList() }) {
            fn(f)
        }
    }

    private val connections = mutableListOf<F>()
}
