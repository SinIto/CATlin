package com.justai.jaicf.template.scenario

import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.model.scenario.Scenario

object Offtopic : Scenario() {

    init {
        state("start") {
            activators {
                regex(".how are you.")
                intent("HowAreYou")
            }
            action {
                reactions.run {
                    image("https://media.giphy.com/media/ICOgUNjpvO0PC/source.gif")
                    sayRandom(
                        "I'm fine",
                        "Hi there! How can I help you?"
                    )
                    // response = {"image": "Smile"};
                    buttons(
                        "Help me!",
                        "How are you?",
                        "What is your name?"
                    )
                }
            }
        }
    }
}