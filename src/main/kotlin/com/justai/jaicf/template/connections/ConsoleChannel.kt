package com.justai.jaicf.template.connections

import com.justai.jaicf.channel.ConsoleChannel
import com.justai.jaicf.template.templateBot

fun main(args: Array<String>) {
    ConsoleChannel(templateBot).run("/start")
}