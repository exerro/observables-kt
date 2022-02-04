package me.exerro.observables

/** An [ObservableConnection] represents a callback connection to something
 *  observable, with a single method to [disconnect] that callback. */
fun interface ObservableConnection {
    /** Disconnect the callback associated with this connection. */
    fun disconnect()

    /** @see ObservableConnection */
    companion object {
        /** An [ObservableConnection] which does nothing when disconnected. */
        val blank = ObservableConnection { }

        /** Join multiple [ObservableConnection]s into a single one, which, when
         *  disconnected, disconnects all the child connections.
         *
         *  ```
         *  // example usage
         *  val connection = ObservableConnection.join(listOf(c1, c2))
         *
         *  connection.disconnect()
         *  // c1 disconnected
         *  // c2 disconnected
         *  ```
         *  */
        fun join(connections: Iterable<ObservableConnection>) = ObservableConnection {
            for (connection in connections) {
                connection.disconnect()
            }
        }

        /** Join multiple [ObservableConnection]s into a single one, which, when
         *  disconnected, disconnects all the child connections.
         *
         *  ```
         *  // example usage
         *  val connection = ObservableConnection.join(c1, c2)
         *
         *  connection.disconnect()
         *  // c1 disconnected
         *  // c2 disconnected
         *  ```
         *  */
        fun join(vararg connections: ObservableConnection) =
            join(connections.toList())
    }
}
