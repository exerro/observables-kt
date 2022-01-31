package me.exerro.observables

internal class InternalConnectionManager<F> {
    internal fun add(
        f: F
    ): ObservableConnection {
        synchronized(connections) { connections.add(f) }
        return ObservableConnection {
            synchronized(connections) { connections.remove(f) }
        }
    }

    inline fun forEach(
        fn: (F) -> Unit
    ) {
        for (f in synchronized(connections) { connections.toList() }) {
            fn(f)
        }
    }

    private val connections = mutableListOf<F>()
}
