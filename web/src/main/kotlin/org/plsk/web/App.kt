package org.plsk.web

import org.plsk.cards.Cards

class App {

    val c = Cards("coucou")

    val greeting: String
        get() {
            return "Hello world => ${c}."
        }
}

fun main(args: Array<String>) {
    println(App().greeting)
}
