package com.justai.jaicf.template.scenario

import com.justai.jaicf.activator.caila.CailaIntentActivatorContext
import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.context.BotContext
import com.justai.jaicf.model.scenario.Scenario
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.json

object MainScenario : Scenario() {

    fun CailaIntentActivatorContext.getAnswerFromTemplate(bc: BotContext): String? {
        val toSubst = mapOf(
            "<age>" to bc.client["age"] as String,
            "<other-var>" to bc.client["other-var"] as String
        )

        var answer = topIntent.answer ?: return null
        toSubst.forEach { k, v ->
            answer = answer.replace(k, v)
        }

        return answer
    }
    init {
        state("Greetings") {
            activators {
                regex(".start")
                intent("Greetings")
            }
            action {
                if (context.client["ClientName"] != null){
                    reactions.go("/Names")
                }
                reactions.run {
                    // image("https://media.giphy.com/media/ICOgUNjpvO0PC/source.gif")
                    sayRandom(
                        "Hello! You opened me! That means that you are a Kotlin developer or interested in Kotlin. What is your name?",
                        "Hi! I am glad to see you! I think you are interested in Kotlin or a newbie in JAICF! What is your name?",
                        "Here you are! Do you like programming or are interested in Kotlin  or JAICF? Me too. What is your name?"
                    )
                    // buttons(
                    //     "Help me!",
                    //     "How are you?",
                    //     "What is your name?"
                    // )
                }
                context.client["bar"] = 0
                // reactions.aimybox?.response?.data?.put("key", json { "some nested key" to "some nested value" })
                reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinSmile.png"))
                // CatlinSadCry.png
                // CatlinStars.png
                // CatlinHearts.png
                reactions.aimybox?.response?.data?.put("bar", context.client["bar"] as JsonElement)
            }

            state("Names") {
                activators {
                    intent("Names")
                    regex("@\"^\b[a-zA-Z0-9_]+\b$") // "\\w+"
                }
                action {
                    val cailaName = activator.caila?.entities?.find { it.entity == "Names" }?.value
                    if (cailaName != null) {
                        context.client["ClientName"] = JsonPrimitive(cailaName)
                    }
                    if (context.client["ClientName"] != null){
                        context.client["bar"] = this.context.client["bar"] + 16
                        reactions.aimybox?.response?.data?.put("bar", context.client["bar"] as JsonElement)
                        if (context.client["bar"] >= 96) {
                            context.client["bar"] = 100
                            reactions.aimybox?.response?.data?.put("bar", context.client["bar"] as JsonElement)
                            reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                        } // CatlinSadCry.png // CatlinStars.png
                        reactions.sayRandom(
                            "Nice to hear you "  + context.client["ClientName"] + "! I’m Catlin. I was designed to cheer up programmers while coding in Kotlin. I know that it can be stressful sometimes. So you can complain to me about everything. I can also tell you some interesting things about Kotlin. May I ask you about your level of Kotlin knowledge? Are you a newbie?",
                            "Hey, "  + context.client["ClientName"] + ", glad to meet. I’m Catlin. My mission is to support programmers in learning Kotlin emotionally. It can be stressful and painful sometimes. May I ask you about your level of Kotlin knowledge? Are you a newbie?",
                            "Beautiful name! Nice to see you " + context.client["ClientName"] + ". I’m Catlin.I was designed to cheer up programmers while coding in Kotlin. I know that it can be stressful sometimes. May I ask you about your level of Kotlin knowledge? Are you a newbie?"
                        )
                        reactions.go("/Level")
                        //reactions.changeState("../asdasda")
                    } else {
                        context.temp["name"] = JsonPrimitive(request.input)
                        reactions.sayRandom(
                            "Are you really have name "  + context.temp["name"] + "?",
                            "Are you really "  + context.temp["name"] + "?"
                        )
                    }
                    // reactions.image("https://media.giphy.com/media/EE185t7OeMbTy/source.gif")
                }

                state("Yes") {
                    activators {
                        intent("Yes")
                    }
                    action {
                        context.client["ClientName"] = context.temp["name"]
                        reactions.go("/Level")
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
                        reactions.changeState("/Names")
                    }
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
                "Sorry, I didn't get that...",
                "Sorry, could you repeat please?"
            )
        }
    }
    }