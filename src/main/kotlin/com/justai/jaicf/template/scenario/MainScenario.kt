package com.justai.jaicf.template.scenario

import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.model.scenario.Scenario
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.json

object MainScenario : Scenario() {

    init {
        state("start") {
            activators {
                regex(".start")
                intent("Hello")
            }
            action {
                reactions.run {
                    // image("https://media.giphy.com/media/ICOgUNjpvO0PC/source.gif")
                    sayRandom(
                        "Hello! How can I help?",
                        "Hi there! How can I help you?"
                    )
                    // buttons(
                    //     "Help me!",
                    //     "How are you?",
                    //     "What is your name?"
                    // )
                }
                // reactions.aimybox?.response?.data?.put("key", json { "some nested key" to "some nested value" })
                // reactions.aimybox?.response?.data?.put("boolean", JsonPrimitive(false))
                reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinSmile.png"))
                // CatlinSadCry.png
                // CatlinStars.png
                // CatlinHearts.png
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(0))
            }
        }

        state("bye") {
            activators {
                intent("Bye")
            }

            action {
                reactions.sayRandom(
                    "See you soon!",
                    "Bye-bye!"
                )
                // reactions.image("https://media.giphy.com/media/EE185t7OeMbTy/source.gif")
            }
        }

        state("smalltalk", noContext = true) {
            activators {
                anyIntent()
            }

            action {
                activator.caila?.topIntent?.answer?.let {
                    reactions.say(it)
                }
            }
        }

        state("help") {
            activators {
                regex(".help")
                regex(".menu")
                intent("Help")
            }

            action {
                reactions.sayRandom(
                    "I can that:",
                    "To control:"
                )
                reactions.say(
                    "1. \"\\start \" to go to start" +
                            "\n 2. \"\\help\" to get some information" +
                            "\n 3. \"\\reset\" to start a new game"
                )
                reactions.image("https://media.giphy.com/media/EE185t7OeMbTy/source.gif")
            }
        }

        fallback {
            reactions.sayRandom(
                "Sorry, I didn't get that...",
                "Sorry, could you repeat please?"
            )
        }
    }
    }