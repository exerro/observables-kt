package me.exerro.observables

/** Filter items of an [ObservableStream] only of type [T]. */
inline fun <reified T> ObservableStream<*>.filterIsInstance() =
    ObservableStream { fn -> connect { if (it is T) fn(it) } }
