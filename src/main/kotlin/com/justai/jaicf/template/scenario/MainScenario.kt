package com.justai.jaicf.template.scenario

import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.model.scenario.Scenario
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

object MainScenario : Scenario() {

    // reactions.aimybox?.response?.data?.put("key", json { "some nested key" to "some nested value" })

    // image("https://media.giphy.com/media/ICOgUNjpvO0PC/source.gif")

    // buttons(
    //     "Help me!",
    //     "How are you?",
    //     "What is your name?"
    // )
    init {
        state("Greetings") {
            activators {
                regex(".start")
                intent("Greetings")
            }
            action {
                context.client["bar"] = 0
                reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinSmile.png"))
                reactions.aimybox?.response?.data?.put("bar", context.client["bar"] as JsonElement)
                if (context.client["ClientName"] != null){
                    reactions.go("/Greetings/Name/Names")
                }
                reactions.run {
                    sayRandom(
                        "Hello! You opened me! That means that you are a Kotlin developer or interested in Kotlin. What is your name?",
                        "Hi! I am glad to see you! I think you are interested in Kotlin or a newbie in JAICF! What is your name?",
                        "Here you are! Do you like programming or are interested in Kotlin  or JAICF? Me too. What is your name?"
                    )

                }
                reactions.changeState("/Greetings/Name")
            }
            state("Name", modal = true) {
                state("Names") {
                    activators {
                        intent("Names")
                    }
                    action {
                        val cailaName = activator.caila?.entities?.find { it.entity == "Names" }?.value
                        if (cailaName != null) {
                            context.client["ClientName"] = cailaName
                        }
                        if (context.client["ClientName"] != null) {
                            context.client["bar"] = context.client["bar"] as Int + 16
                            reactions.aimybox?.response?.data?.put("bar", context.client["bar"] as JsonElement)
                            if (context.client["bar"] as Int >= 96) {
                                context.client["bar"] = 100
                                reactions.aimybox?.response?.data?.put("bar", context.client["bar"] as JsonElement)
                                reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                            } // CatlinSadCry.png // CatlinStars.png
                            reactions.sayRandom(
                                "Nice to hear you " + context.client["ClientName"] as String + "! I’m Catlin. I was designed to cheer up programmers while coding in Kotlin. I know that it can be stressful sometimes. So you can complain to me about everything. I can also tell you some interesting things about Kotlin. May I ask you about your level of Kotlin knowledge? Are you a newbie?",
                                "Hey, " + context.client["ClientName"] as String + ", glad to meet. I’m Catlin. My mission is to support programmers in learning Kotlin emotionally. It can be stressful and painful sometimes. May I ask you about your level of Kotlin knowledge? Are you a newbie?",
                                "Beautiful name! Nice to see you " + context.client["ClientName"] as String + ". I’m Catlin.I was designed to cheer up programmers while coding in Kotlin. I know that it can be stressful sometimes. May I ask you about your level of Kotlin knowledge? Are you a newbie?"
                            )
                            reactions.go("/Level")
                        } else {
                            context.session["name"] = request.input
                            reactions.sayRandom(
                                "Are you really have name " + context.session["name"] + "?",
                                "Are you really " + context.session["name"] + "?"
                            )
                        }
                    }
                    state("Yes") {
                        activators {
                            intent("Yes")
                        }
                        action {
                            context.client["ClientName"] = context.session["name"]
                            reactions.go("/Greetings/Name/Names")
                        }
                    }
                    state("No") {
                        activators {
                            intent("No")
                        }
                        action {
                            reactions.sayRandom(
                                "What is your name, really?",
                                "How can I call you?"
                            )
                            reactions.changeState("/Greetings/Name/Names")
                        }
                    }
                }

                fallback {
                    context.temp["name"] = request.input
                    reactions.go("/Greetings/Name/Names")
                }
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
                // reactions.image("https://media.giphy.com/media/EE185t7OeMbTy/source.gif")
            }
        }

        state("reset") {
            activators {
                regex(".reset")
                intent("Reset")
            }

            action {
                context.client["ClientName"] = null
                reactions.sayRandom(
                    "Ok, again.",
                    "Go to the start!"
                )
                reactions.go("/Greetings")
            }
        }


        fallback {
            reactions.sayRandom(
                "Sorry, I can’t answer you to that, but we can talk about music or movies or my favourite programming language.",
                "My creators didn’t teach me that, but we can speak about JAICF or Just AI products. You can also tell me about your problems.",
                "I am sorry, but I am too young to talk about that."
            )
        }
    }
    }