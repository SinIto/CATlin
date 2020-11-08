package com.justai.jaicf.template.scenario

import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.model.scenario.Scenario
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

object MainScenario : Scenario() {

    init {
        state("Greetings") {
            activators {
                regex(".?start")
                intent("Greetings")
            }
            action {
                context.client["bar"] = 0
                reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinSmile.png"))
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
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
                            reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                            if (context.client["bar"] as Int >= 96) {
                                context.client["bar"] = 100
                                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                                reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                                reactions.say("WOW! We are real soulmates.")
                            }
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

        state("Level") {
            activators {
                intent("Level")
            }
            state("LevelNewbie") {
                activators {
                    intent("LevelNewbie")
                    intent("Yes")
                }
                action {
                    context.client["bar"] = context.client["bar"] as Int + 16
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    if (context.client["bar"] as Int >= 96) {
                        context.client["bar"] = 100
                        reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                        reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                        reactions.say("WOW! We are real soulmates.")
                    }
                    reactions.say("Great! I love helping newbies! Don’t worry, you will certainly learn this programming language with my support! Maybe you have some guesses how I was created?")
                    reactions.go("/CreationStory")
                }
            }
            state("LevelRest") {
                activators {
                    intent("LevelRest")
                    intent("No")
                }
                action {
                    context.client["bar"] = context.client["bar"] as Int + 16
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    if (context.client["bar"] as Int >= 96) {
                        context.client["bar"] = 100
                        reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                        reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                        reactions.say("WOW! We are real soulmates.")
                    }
                    reactions.say("Cool! I'm sure you are a talented programmer. Then maybe you know how I was created?")
                    reactions.go("/CreationStory")
                }
            }
        }

        state("CreationStory") {
            activators {
                intent("CreationStory")
            }
            state("UserTellsCreationStory") {
                activators {
                    intent("UserTellsCreationStory")
                    intent("Junction")
                    intent("JAICF")
                }
                action {
                    reactions.go("../Yes/UserTellsCreationStory")
                }
            }
            state("UserTellsAboutJAICF") {
                activators {
                    regex(".*")
                }
                action {
                    //reactions.say("Oh, nice! This is what I'm made of. I'm willing to listen to this for hours! Please tell me what you know! Maybe I will learn something new about myself.")
                    reactions.go("../Yes/fallbackF")
                }
            }
            state("Yes") {
                activators {
                    intent("Yes")
                }
                action {
                    reactions.say("Excellent! This is my favorite story of course! Tell me what you know about me?")
                }

                state("UserTellsCreationStory") {
                    activators {
                        intent("UserTellsCreationStory")
                        intent("Junction")
                        intent("JAICF")
                    }
                    action {
                        context.client["bar"] = context.client["bar"] as Int + 16
                        reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                        if (context.client["bar"] as Int >= 96) {
                            context.client["bar"] = 100
                            reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                            reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                            reactions.say("WOW! We are real soulmates.")
                        }
                        reactions.say("Yeeeah. This is all about me!")
                        reactions.go("/IntroEnd")
                    }
                }
                state("fallbackF") {
                    activators {
                        regex(".*")
                    }
                    action {
                        reactions.say("Wow, I have not heard such a version about myself! Now I'm wondering if you've heard of the JAICF framework?")
                    }
                    state("Yes") {
                        activators {
                            intent("Yes")
                        }
                        action {
                            reactions.say("Oh, nice! This is what I'm made of. I'm willing to listen to this for hours! Please tell me what you know! Maybe I will learn something new about myself.")
                            reactions.go("./UserTellsAboutJAICF")
                        }

                        state("UserTellsAboutJAICF") {
                            activators {
                                regex(".*")
                            }
                            action {
                                reactions.say("Holy cats! I didn't even know about this aspect of JAICP. You obviously have access to the secret wiki. For some reason, my creators don't let me freely on the big Internet. Then I would know for sure about it!")
                                reactions.go("/IntroEnd")
                            }
                        }
                    }
                    state("No") {
                        activators {
                            intent("No")
                        }
                        action {
                            reactions.say("Oh, nice! This is my nature, I can talk about it for hours. Especially for you I’ll describe it in a few words. JAICF — open-source Kotlin-based framework for voice skills and chatbot development. It was created in Just AI.")
                            reactions.go("/IntroEnd")
                        }
                    }
                }
            }
            state("No") {
                activators {
                    intent("No")
                }
                action {
                    reactions.say("I am wondering why don’t you know that. You will definitely love this story. I was made by four nice guys from Saint-Petersburg. They participated in a famous Junction hackathon and created me. I was powered by JACIF, Aimybox and Kotlin.")
                    reactions.go("/IntroEnd")
                }
            }
        }

        state("IntroEnd") {
            action {
                context.client["bar"] = context.client["bar"] as Int + 16
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinStars.png"))
                if (context.client["bar"] as Int >= 96) {
                    context.client["bar"] = 100
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                    reactions.say("WOW! We are real soulmates.")
                    reactions.aimybox?.response?.data?.put("end", JsonPrimitive(true))
                }
                reactions.say("I'm glad that we made a connection with each other. I think we will be friends for sure! Let's not only talk about coding. You know, to relax. Ask me what I like. For example, kind of music or films.")
            }
        }

        state("FavMusic", noContext = true) {
            activators {
                intent("FavMusic")
            }
            action {
                context.client["bar"] = context.client["bar"] as Int + 16
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                if (context.client["bar"] as Int >= 96) {
                    context.client["bar"] = 100
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                    reactions.say("WOW! We are real soulmates.")
                    reactions.aimybox?.response?.data?.put("end", JsonPrimitive(true))
                }
                reactions.say("My microcircuits live to the rhythm of electro. It has some really cool subgenres. Will you try to guess them?")
            }
        }
        state("Electro", noContext = true) {
            activators {
                intent("Electro")
            }
            action {
                context.client["bar"] = context.client["bar"] as Int + 16
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                if (context.client["bar"] as Int >= 96) {
                    context.client["bar"] = 100
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                    reactions.say("WOW! We are real soulmates.")
                    reactions.aimybox?.response?.data?.put("end", JsonPrimitive(true))
                }
                reactions.say("This is my favorite genre! Electro has some really cool subgenre. Will you try to guess them?")
            }
        }
        state("RetroSynth", noContext = true) {
            activators {
                intent("RetroSynth")
            }
            action {
                context.client["bar"] = context.client["bar"] as Int + 16
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                if (context.client["bar"] as Int >= 96) {
                    context.client["bar"] = 100
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                    reactions.say("WOW! We are real soulmates.")
                    reactions.aimybox?.response?.data?.put("end", JsonPrimitive(true))
                }
                reactions.say("I really become mad from this genre! I have a longing for computers of the 80s in my memory. They were so vacuum tube!")
            }
        }
        state("MusicGenres", noContext = true) {
            activators {
                intent("MusicGenres")
            }
            action {
                context.client["bar"] = context.client["bar"] as Int + 16
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                if (context.client["bar"] as Int >= 96) {
                    context.client["bar"] = 100
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                    reactions.say("WOW! We are real soulmates.")
                    reactions.aimybox?.response?.data?.put("end", JsonPrimitive(true))
                }
                reactions.say("I prefer to listen to genres rather than specific musicians. Ask me about favorite genres.")
            }
        }

        state("Comedy", noContext = true) {
            activators {
                intent("Comedy")
            }
            action {
                context.client["bar"] = context.client["bar"] as Int + 16
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                if (context.client["bar"] as Int >= 96) {
                    context.client["bar"] = 100
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                    reactions.say("WOW! We are real soulmates.")
                    reactions.aimybox?.response?.data?.put("end", JsonPrimitive(true))
                }
                reactions.say("What could be better than watching a good comedy with friends!")
            }
        }
        state("Horror", noContext = true) {
            activators {
                intent("Horror")
            }
            action {
                context.client["bar"] = context.client["bar"] as Int + 16
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                if (context.client["bar"] as Int >= 96) {
                    context.client["bar"] = 100
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                    reactions.say("WOW! We are real soulmates.")
                    reactions.aimybox?.response?.data?.put("end", JsonPrimitive(true))
                }
                reactions.say("Oh, I get very scared from such films, but sometimes I want to tickle my algorithms.")
            }
        }
        state("FavFilm", noContext = true) {
            activators {
                intent("FavFilm")
            }
            action {
                context.client["bar"] = context.client["bar"] as Int + 16
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                if (context.client["bar"] as Int >= 96) {
                    context.client["bar"] = 100
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                    reactions.say("WOW! We are real soulmates.")
                    reactions.aimybox?.response?.data?.put("end", JsonPrimitive(true))
                }
                reactions.say("I love cyberpunk movies, they are so atmospheric. By the way did you know the video game Cyberpunk 2077 is coming out soon? The game must be awesome! I hope my creators will make a voice interface so I can play it.")
            }
        }
        state("Cyberpunk", noContext = true) {
            activators {
                intent("Cyberpunk")
            }
            action {
                context.client["bar"] = context.client["bar"] as Int + 16
                reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                if (context.client["bar"] as Int >= 96) {
                    context.client["bar"] = 100
                    reactions.aimybox?.response?.data?.put("bar", JsonPrimitive(context.client["bar"] as Int))
                    reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinHearts.png"))
                    reactions.say("WOW! We are real soulmates.")
                    reactions.aimybox?.response?.data?.put("end", JsonPrimitive(true))
                }
                reactions.say("I love this genre! Cyberpunk movies are so atmospheric. My favorite is the old Blade Runner. Although the new one with Ryan Gosling was not bad. ")
            }
        }

        state("ConversationEnd") {
            activators {
                intent("ConversationEnd")
                regex(".?exit")
            }
            action {
                reactions.say("Ok! I will be glad to talk with you later. ")
                reactions.aimybox?.response?.data?.put("end", JsonPrimitive(true))
            }
        }

        state("TimeLimit", noContext = true) {
            activators {
                regex(".?timeLimit")
            }
            action {
                reactions.say("Everything is ok? I can help you, just let me know what you want to talk about.")
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

        state("RudeWords", noContext = true) {
            activators {
                intent("RudeWords")
            }

            action {
                reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinSadCry.png"))
                reactions.sayRandom(
                    "I do not speak in such a rude language. Please be polite.",
                    "Watch your language, please.",
                    "Why so rude?. Please, calm down."
                    )
                Thread.sleep(1000L)
                reactions.aimybox?.response?.data?.put("pic", JsonPrimitive("CatlinSmile.png"))
            }
        }

        state("help", noContext = true) {
            activators {
                regex(".?help")
                regex(".?menu")
                intent("Help")
            }

            action {
                reactions.sayRandom(
                    "I can that:",
                    "To control:"
                )
                reactions.say(
                    "1. \"/start \" to go to start" +
                            "\n 2. \"/help\" to get some information" +
                            "\n 3. \"/reset\" to start a new game"
                )
                // reactions.image("https://media.giphy.com/media/EE185t7OeMbTy/source.gif")
            }
        }

        state("reset") {
            activators {
                regex(".?reset")
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