package observables

/** An object that may be connected to with a function of type `F`. */
interface Connectable<F> {
    fun connect(fn: F): F
    fun disconnect(fn: F)
    fun disconnectAll()
    /** Create a connection manager for this connectable. */
    fun manager() = ConnectionManager(this)
}

////////////////////////////////////////////////////////////////////////////////////////////////////

/** Is a manager of connectable that connects to a signal.
 *  When disconnectAll() is invoked, all connections made to the signal through
 *  this manager will be disconnected.
 *
 *  Allows keeping track of connections made to a signal and removing them all
 *  in one go. */
class ConnectionManager<F>(private val signal: Connectable<F>): Connectable<F> {
    override fun connect(fn: F) = synchronized(connections) { signal.connect(fn) }
    override fun disconnect(fn: F) = synchronized(connections) { signal.disconnect(fn) }
    override fun disconnectAll() = synchronized(connections) {
        connections.forEach(signal::disconnect) }

    private val connections = mutableListOf<F>()
}

////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * A signal is similar to an event.
 *
 * One may connect functions (callbacks) to the signal, and they will be invoked
 * when the signal is "emitted".
 *
 * A UnitSignal is a variant of signal that takes no value.
 * This class is thread-safe.
 */
@Deprecated("Outdated", replaceWith = ReplaceWith("UnitSubscribable"))
class UnitSignal: Connectable<() -> Unit> {
    /** Connect a callback to the signal. The callback will be invoked when
     *  the signal is emitted. */
    override fun connect(fn: () -> Unit): () -> Unit {
        synchronized(connections) { connections.add(fn) }
        return fn
    }

    /** Disconnect a callback from the signal. The callback will no longer be
     *  invoked when the signal is emitted. */
    override fun disconnect(fn: () -> Unit) { synchronized(connections) { connections.remove(fn) } }

    /** Disconnect all callbacks from the signal. Nothing will be invoked when
     *  the signal is emitted, until another callback is connected. */
    override fun disconnectAll() { synchronized(connections) { connections.clear() } }

    /** Emit the signal, triggering all callbacks with the data provided. */
    fun emit() { synchronized(connections) { connections.toList().forEach { it() } } }

    private val connections = mutableListOf<() -> Any?>()
}

/**
 * A signal is similar to an event.
 *
 * One may connect functions (callbacks) to the signal, and they will be invoked
 * when the signal is "emitted", with the data provided.
 *
 * A Signal<T> is a variant of signal that takes a value of type T.
 * This class is thread-safe.
 */
@Deprecated("Outdated", replaceWith = ReplaceWith("Subscribable<T>"))
open class Signal<T>: Connectable<(T) -> Unit> {
    /** Connect a callback to the signal. The callback will be invoked when
     *  the signal is emitted. */
    override fun connect(fn: (T) -> Unit): (T) -> Unit {
        synchronized(connections) { connections.add(fn) }
        return fn
    }

    /** Disconnect a callback from the signal. The callback will no longer be
     *  invoked when the signal is emitted. */
    override fun disconnect(fn: (T) -> Unit) { synchronized(connections) { connections.remove(fn) } }

    /** Disconnect all callbacks from the signal. Nothing will be invoked when
     *  the signal is emitted, until another callback is connected. */
    override fun disconnectAll() { synchronized(connections) { connections.clear() } }

    /** Emit the signal, triggering all callbacks with the data provided. */
    fun emit(v: T) { synchronized(connections) { connections.toList().forEach { it(v) } } }

    private val connections = mutableListOf<(T) -> Any?>()
}

/**
 * A signal is similar to an event.
 *
 * One may connect functions (callbacks) to the signal, and they will be invoked
 * when the signal is "emitted", with the data provided.
 *
 * A BiSignal<A, B> is a variant of signal that takes values of types A and B.
 * This class is thread-safe.
 */
@Deprecated("Outdated", replaceWith = ReplaceWith("BiSubscribable<A, B>"))
open class BiSignal<A, B>: Connectable<(A, B) -> Unit> {
    /** Connect a callback to the signal. The callback will be invoked when
     *  the signal is emitted. */
    override fun connect(fn: (A, B) -> Unit): (A, B) -> Unit {
        synchronized(connections) { connections.add(fn) }
        return fn
    }

    /** Disconnect a callback from the signal. The callback will no longer be
     *  invoked when the signal is emitted. */
    override fun disconnect(fn: (A, B) -> Unit) { synchronized(connections) { connections.remove(fn) } }

    /** Disconnect all callbacks from the signal. Nothing will be invoked when
     *  the signal is emitted, until another callback is connected. */
    override fun disconnectAll() { synchronized(connections) { connections.clear() } }

    /** Emit the signal, triggering all callbacks with the data provided. */
    fun emit(a: A, b: B) { synchronized(connections) { connections.toList().forEach { it(a, b) } } }

    private val connections = mutableListOf<(A, B) -> Any?>()
}
