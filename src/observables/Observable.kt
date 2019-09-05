package observables

/** An observed value which emits a signal when changed. */
class Observable<T>(value: T): Connectable<(T) -> Unit> {
    /** observables.Signal emitted when the value is changed, with the new value as a
     *  parameter */
    val changed = Signal<T>()

    /** The current value of the object. */
    var value: T = value
        private set

    /** Set the new value of the object, emitting the changed signal. */
    fun value(value: T): T {
        var hasChanged = false

        synchronized(lock) {
            if (this.value != value) {
                this.value = value
                hasChanged = true
            }
        }

        if (hasChanged) changed.emit(value)

        return value
    }

    /** Set the new value of the object, emitting the changed signal. */
    operator fun invoke(value: T) = value(value)

    /** Connect a callback to the changed signal, and call it once immediately
     *  with the current value of the object. */
    override fun connect(fn: (T) -> Unit)
            = changed.connect(fn).also { fn(value) }

    /** Disconnect a callback from the changed signal. */
    override fun disconnect(fn: (T) -> Unit)
            = changed.disconnect(fn)

    /** Disconnect all callbacks from the changed signal. */
    override fun disconnectAll()
            = changed.disconnectAll()

    private val lock = object {}
}
