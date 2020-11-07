package com.justai.jaicf.template.scenario

import com.justai.jaicf.activator.caila.CailaIntentActivatorContext
import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.context.BotContext
import com.justai.jaicf.model.scenario.Scenario
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
                reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinSmile.png"))
                // CatlinSadCry.png
                // CatlinStars.png
                // CatlinHearts.png
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(0))
            }

            state("Names") {
                activators {
                    intent("Names")
                    regex("\\w+")
                }
                action {
                    val cailaName = activator.caila?.entities?.find { it.entity == "Names" }?.value

                    if (cailaName != null){
                        reactions.sayRandom(
                            "Nice to hear you "  + cailaName + "! I’m Catlin. I was designed to cheer up programmers while coding in Kotlin. I know that it can be stressful sometimes. So you can complain to me about everything. I can also tell you some interesting things about Kotlin. May I ask you about your level of Kotlin knowledge? Are you a newbie?",
                            "Hey, "  + cailaName + ", glad to meet. I’m Catlin. My mission is to support programmers in learning Kotlin emotionally. It can be stressful and painful sometimes. May I ask you about your level of Kotlin knowledge? Are you a newbie?",
                            "Beautiful name! Nice to see you " + cailaName + ". I’m Catlin.I was designed to cheer up programmers while coding in Kotlin. I know that it can be stressful sometimes. May I ask you about your level of Kotlin knowledge? Are you a newbie?"
                        )
                        reactions.go("/Level")
                        //reactions.changeState("../asdasda")
                    } else {
                        context?.session?.put("name",JsonPrimitive(request.input))
                        reactions.sayRandom(
                            "Are you realy have name "  + context.session["name"] + "?",
                            "Are you realy "  + context.session["name"] + "?"
                        )
                    }
                    // reactions.image("https://media.giphy.com/media/EE185t7OeMbTy/source.gif")
                }

                state("Yes") {
                    activators {
                        intent("Yes")
                    }
                    action {
                        //BotContext.client["name"], JsonPrimitive(name))
                        reactions.sayRandom(
                            "Are you really have name "  + context.session["name"] + "?",
                            "Are you really "  + context.session["name"] + "?"
                        )
                        // reactions.image("https://media.giphy.com/media/EE185t7OeMbTy/source.gif")
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