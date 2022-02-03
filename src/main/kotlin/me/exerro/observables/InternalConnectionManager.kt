package me.exerro.observables

/** Manages a list of connections of type [F] in a thread-safe manner. */
internal class InternalConnectionManager<F> {
    /** Add a callback, returning an [ObservableConnection] which will later
     *  remove it. */
    internal fun add(f: F): ObservableConnection {
        synchronized(connections) { connections.add(f) }
        return ObservableConnection {
            synchronized(connections) { connections.remove(f) }
        }
    }

    /** Run [F] with each callback. */
    inline fun forEach(fn: (F) -> Unit) {
        for (f in synchronized(connections) { connections.toList() }) {
            fn(f)
        }
    }

    private val connections = mutableListOf<F>()
}
