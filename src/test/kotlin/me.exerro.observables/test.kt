package me.exerro.observables

/** Run all tests in this directory. */
fun main() {
    testObservableConnections()

    testObservableStreamCreate()
    testObservableStreamOf()
    testObservableStreamMap()
    testObservableStreamFilter()
    testObservableStreamFilterIsInstance()
    testObservableStreamFlatMap()
    testObservableStreamFold()

    testObservableValueCreate()
    testObservableValueCreateLateInit()

    testUnitSignalCallbackCalled()
    testUnitSignalDisconnect()
    testMultipleConnections()
    testSignalValues()
    testSignalsOf()
}
