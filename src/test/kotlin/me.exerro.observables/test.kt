package me.exerro.observables

/** Run all tests in this directory. */
fun main() {
    testObservableConnections()

    testUnitSignalCallbackCalled()
    testUnitSignalDisconnect()
    testMultipleSignalConnections()
    testSignalValues()
    testSignalsOf()

    testObservableStreamCreate()
    testObservableStreamOf()
    testObservableStreamMap()
    testObservableStreamFilter()
    testObservableStreamFilterIsInstance()
    testObservableStreamFlatMap()
    testObservableStreamFold()

    testObservableValueOf()
    testObservableValueCreate()
    testObservableValueCreateLateInit()
}
