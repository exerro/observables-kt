import observables.Subscribable1
import observables.subscribeTo

val a = Subscribable1<Int>()

fun test() {
    val owner = Object()

    owner.subscribeTo(a) {
        println("$it + $this is alive")
    }

    a.emit(3)
}

fun main() {
    test()
    a.emit(2)
    System.gc()
    a.emit(1)
}
