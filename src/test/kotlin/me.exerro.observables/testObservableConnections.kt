package me.exerro.observables

/** Test that [ObservableConnection.join] returns an [ObservableConnection]
 *  which disconnects its parameters. */
fun testObservableConnections() {
    var c1d = false
    var c2d = false

    val c1 = ObservableConnection { c1d = true }
    val c2 = ObservableConnection { c2d = true }

    ObservableConnection.join(c1, c2).disconnect()

    assert(c1d && c2d) { "Both connections were not disconnected" }

    println("Both connections were disconnected")
}
