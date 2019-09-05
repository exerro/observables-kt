# observables-kt

Implement observable values and signals for Kotlin.

## Signals

A signal is similar to an event.

One may connect functions (callbacks) to the signal, and they will be invoked
when the signal is "emitted".

Some variants of signals take parameters as well, which are given to emit and
passed to the callbacks connected when invoked.

Signals are all thread-safe.

## Observables

An `Observable<T>` is a wrapper around a value of type `T`  which has a
single `changed` signal that is emitted with the new value of the object
when changed.

An observable is itself a `Connectable` (acting like a signal), where
connecting directly to the observable will also invoke the callback
immediately with the current value of the observable.
