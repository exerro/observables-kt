<h1 align="center">
  observables-kt
</h1>

<p align="center">
  <a href="https://jitpack.io/#exerro/observables-kt"><img src="https://jitpack.io/v/exerro/observables-kt.svg" /></a>
</p>

Implements observable values and signals for Kotlin.

```kotlin
val items = ObservableStream
  .of(1, 2, 3)
  .map { it + 1 }
  .filter { it > 2 }

items.connect(::println)
//> 3
//> 4

////////////////////////////////////////////

val value = MutableObservableValue.create(3)

value.connect(::println)
value.currentValue = 2
//> 2
```

## Installation

Check out the [releases](https://github.com/exerro/observables-kt/releases), or
using a build system...

### Gradle (`build.gradle.kts`)

```kotlin
repositories {
    // ...
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("me.exerro:observables-kt:1.2.0")
}
```

### Maven

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>me.exerro</groupId>
  <artifactId>observables-kt</artifactId>
  <version>1.2.0</version>
</dependency>
```

## Observables

The library is centred around two interfaces:
* `Observable` - something which you can connect callbacks to.
* `ObservableStream` - an extension of `Observable`, specialised for streams of 
  values. Can be mapped, filtered, etc.

When a connection is made, an `ObservableConnection` is returned, which can be
used to disconnect the callback. Multiple connections can be joined to form a
single connection using `ObservableConnection.join`.

## ObservableSignals

A signal is similar to an event.

One may connect functions (callbacks) to the signal, and they will be invoked
when the signal is "emitted".

Some variants of signals take parameters as well, which are given to emit and
passed to the callbacks connected when invoked.

You can create signals with the `ObservableSignal.create*Signal` and
`ObservableSignal.create*SignalOf` methods, for example:

```kotlin
val (s, emit) = ObservableSignal.createSignal<Int>()

s.connect { println("Got $it") }

emit(3)
//> Got 3
```

Signals are all thread-safe.

## ObservableStreams

An `ObservableStream` is a stream of values which can be connected to. You can
map, filter, flatMap, and fold these streams using built-in functions. For
example,

```kotlin
ObservableStream
  .of(1, 2, 3, "hello") // 1, 2, 3, "hello"
  .filterIsInstance<Int>() // 1, 2, 3
  .map { it - 1 } // 0, 1, 2
  .filter { it > 0 } // 1, 2
  .flatMap { v -> (1 .. v).map { v } } // 1, 2, 2
  .fold(3) { a, b -> a + b } // [3], 4, 6, 8
  .connect(::println)
//> 4
//> 6
//> 8
```

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
