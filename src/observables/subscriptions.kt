package observables

import java.lang.ref.WeakReference

/** Subscribe to a subscribable using an owner. */
fun <T: Any, P> T.subscribeTo(subscribable: Subscribable<P>, fn: T.(P) -> Unit)
        = subscribable.subscribe(this, fn)

/** Emit nothing. See emit(value). */
fun UnitSubscribable.emit()
        = emit(Unit)

/** Emit two values. See emit(value). */
fun <A, B> BiSubscribable<A, B>.emit(a: A, b: B)
        = emit(a to b)

/** Emit three values. See emit(value). */
fun <A, B, C> TriSubscribable<A, B, C>.emit(a: A, b: B, c: C)
        = emit(Triple(a, b, c))

//////////////////////////////////////////////////////////////////////////////////////////

typealias UnitSubscribable = Subscribable<Unit>
typealias BiSubscribable<A, B> = Subscribable<Pair<A, B>>
typealias TriSubscribable<A, B, C> = Subscribable<Triple<A, B, C>>

//////////////////////////////////////////////////////////////////////////////////////////

/** A subscribable entity. */
class Subscribable<P> {
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
    
    /** Emit a value to be passed to all callbacks previously registered.
     * 
     *  Note: this will remove any subscriptions of garbage collected objects. */
    fun emit(value: P) {
        synchronized(subscriptions) { subscriptions.removeAll { it(value) } }
    }

    ////////////////////////////////////////////////////////////////////////////

    private val subscriptions: MutableList<Subscription<*>> = mutableListOf()
    
    ////////////////////////////////////////////////////////////////////////////

    private inner class Subscription<T: Any>(
            owner: T,
            private val fn: (T, P) -> Unit
    ) {
        internal val ref = WeakReference(owner)
        
        internal operator fun invoke(p: P)
            = ref.get() ?.also { fn(it, p) } == null
    }
}
