# observables-kt

Implement observable values and signals for Kotlin.

## Observables

The library is centred around two interfaces:
* `Observable` - something which you can connect callbacks to.
* `ObservableStream` - an extension of `Observable`, specialised for streams of 
  values. Can be mapped, filtered, etc.

When a connection is made, it returns an `ObservableConnection`, which can be
used to disconnect the callback. Multiple connections can be joined to form a
single connection using `ObservableConnection.join`.

## Signals

A signal is similar to an event.

One may connect functions (callbacks) to the signal, and they will be invoked
when the signal is "emitted".

Some variants of signals take parameters as well, which are given to emit and
passed to the callbacks connected when invoked.

You can create signals with the `Observable.create*Signal` and
`Observable.create*SignalOf` methods, for example:

```kotlin
val (s, emit) = Observable.createSignal<Int>()

s.connect { println("Got $it") }

emit(3)
//> Got 3
```

Signals are all thread-safe.

## ObservableStreams

TODO

## ObservableValues

An `ObservableValue` is a wrapper around a value which also implements
`ObservableStream` - meaning you can connect to changes of the value.

There is a specialised interface `MutableObservableValue` which allows you to
set the value, along with some constructors to help create these.

`ObservableValue`s can act as property delegates, making the following possible:

```kotlin
val value = MutableObservableValue.create(0)
var currentValue: Int by value

println(currentValue)
//> 0

value.connect { println("Changed to $it") }

currentValue = 1
//> Changed to 1
```
