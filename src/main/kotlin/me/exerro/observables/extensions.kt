package me.exerro.observables

/** Filter items of an [ObservableStream] only of type [T].
 *
 *  ```
 *  // example usage
 *  val mySpecialEvents = eventStream.filterIsInstance<MyEvent>()
 *  ```
 *  @see ObservableStream.filter
 *  */
inline fun <reified T> ObservableStream<*>.filterIsInstance() =
    ObservableStream { fn -> connect { if (it is T) fn(it) } }
