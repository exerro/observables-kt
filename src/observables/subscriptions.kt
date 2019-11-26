package observables

import java.lang.ref.WeakReference

/** A subscribable entity with no value emitted. */
class Subscribable0: Subscribable<Unit>() {
    /** Emit a signal with no value. */
    fun emit() = invokeAll(Unit)
}

/** A subscribable entity with a single value emitted. */
class Subscribable1<T>: Subscribable<T>() {
    /** Emit a value. */
    fun emit(value: T) = invokeAll(value)
}

/** A subscribable entity with two values emitted. */
class Subscribable2<A, B>: Subscribable<Pair<A, B>>() {
    /** Emit two values. */
    fun emit(a: A, b: B) = invokeAll(a to b)
}

//////////////////////////////////////////////////////////////////////////////////////////

/** Subscribe to a subscribable using an owner. */
fun <T: Any> T.subscribeTo(subscribable: Subscribable0, fn: T.() -> Unit)
        = subscribable.subscribe(this) { fn(this) }

/** Subscribe to a subscribable using an owner. */
fun <T: Any, P> T.subscribeTo(subscribable: Subscribable1<P>, fn: T.(P) -> Unit)
        = subscribable.subscribe(this, fn)

/** Subscribe to a subscribable using an owner. */
fun <T: Any, A, B> T.subscribeTo(subscribable: Subscribable2<A, B>, fn: T.(A, B) -> Unit)
        = subscribable.subscribe(this) { (a, b) -> fn(this, a, b) }

//////////////////////////////////////////////////////////////////////////////////////////

/** A subscribable entity. */
open class Subscribable<P> {
    /** Subscribe to the entity, registering fn to be called whenever values
     *  are emitted by the entity.
     *  
     *  Note that, if the owner is garbage collected, the callback fn will be
     *  removed automatically when a signal is next emitted. */
    fun <O: Any> subscribe(owner: O, fn: O.(P) -> Unit) {
        synchronized(subscriptions) { subscriptions.add(Subscription(owner, fn)) }
    }

    /** Unsubscribe from the entity, unregistering any fn previously subscribed
     *  using the owner given. */
    fun <O: Any> unsubscribe(owner: O) {
        synchronized(subscriptions) { subscriptions.removeAll { it.ref.get() == owner } }
    }
    
    protected fun invokeAll(data: P) {
        synchronized(subscriptions) {
            subscriptions.removeAll { it(data) }
        }
    }

    private val subscriptions: MutableList<Subscription<*>> = mutableListOf()
    
    ////////////////////////////////////////////////////////////////////////////

    protected inner class Subscription<T: Any>(
            owner: T,
            private val fn: (T, P) -> Unit
    ) {
        internal val ref = WeakReference(owner)
        
        internal operator fun invoke(p: P)
            = ref.get() ?.also { fn(it, p) } == null
    }
}
