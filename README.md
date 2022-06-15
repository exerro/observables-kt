<h1 align="center">
  observables-kt
</h1>

<p align="center">
  <a href="https://jitpack.io/#exerro/observables-kt"><img src="https://jitpack.io/v/exerro/observables-kt.svg" alt="JitPack badge"/></a>
</p>

Implements observable values and signals for Kotlin. Observables let you connect
callbacks which are called in response to various events. Observable values
addionally track a value.

```kotlin
val items = ObservableStream
  .of(1, 2, 3) // 1, 2, 3
  .map { it + 1 } // 2, 3, 4
  .filter { it > 2 } // 3, 4

items.connect { item ->
	println("Got $item")
}
//> Got 3
//> Got 4

////////////////////////////////////////////

val value = MutableObservableValue.of(3)

value.connect { newValue ->
	println("Got $newValue")
}

value.currentValue = 2
//> Got 2
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

The library is centred around three interfaces:
* `Observable` - something which you can connect callbacks to.
* `ObservableStream` - an extension of `Observable`, specialised for streams of 
  values. Can be mapped, filtered, etc.
* `ObservableValue` - an extension of `ObservableStream` which also holds a
  current value.

When a callback is connected, an `ObservableConnection` is returned which can be
used to disconnect the callback later. Multiple connections can be joined using
`ObservableConnection.join` to form a single connection.

## `ObservableStream`s

An `ObservableStream` is a stream of values which can be connected to. Any value
that flows "through this stream" is passed to all connected callbacks. Using
`map`, `filter`, `flatMap`, and `fold` will return a new stream with the
operation applied. For example,

```kotlin
ObservableStream
  .of(1, 2, 3, "hello") // 1, 2, 3, "hello"
  .filterIsInstance<Int>() // 1, 2, 3
  .map { it - 1 } // 0, 1, 2
  .filter { it > 0 } // 1, 2
  .flatMap { v -> List(v) { v } } // 1, 2, 2
  .fold(3) { a, b -> a + b } // [3], 4, 6, 8
  .connect(::println)
//> 4
//> 6
//> 8
```

## `ObservableSignal`s

A signal is a type of `ObservableStream` and is similar to an event. As with
`ObservableStream`s, any connected functions (callbacks) will be called when the
signal is "emitted".

Some variants of signals take parameters. Parameters are provided to `emit` and
passed to the connected functions.

You can create signals with the `ObservableSignal.create*Signal` and
`ObservableSignal.create*SignalOf` methods, for example:

```kotlin
val (stream, emit) = ObservableSignal.createSignal<Int>()

stream.connect { value ->
	println("Got $value")
}

emit(3)
//> Got 3
```

The returned `stream` is a regular `ObservableStream`.

Signals are all thread-safe.

## ObservableValues

An `ObservableValue` is a wrapper around a value which also implements
`ObservableStream` - meaning you can connect to changes of the value.

There is a specialised interface `MutableObservableValue` which allows you to
set the value, along with providing some helpful constructors.

`ObservableValue`s can act as property delegates, making the following possible:

```kotlin
val value = MutableObservableValue.of(0)
var currentValue: Int by value

println(currentValue)
//> 0

value.connect { newValue ->
	println("Changed to $newValue")
}

currentValue = 1
//> Changed to 1
```
