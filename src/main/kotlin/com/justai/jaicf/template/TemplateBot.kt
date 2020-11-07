package com.justai.jaicf.template

import com.justai.jaicf.BotEngine
import com.justai.jaicf.activator.caila.CailaIntentActivator
import com.justai.jaicf.activator.caila.CailaNLUSettings
import com.justai.jaicf.activator.regex.RegexActivator
import com.justai.jaicf.channel.jaicp.DEFAULT_PROXY_URL
import com.justai.jaicf.channel.jaicp.logging.JaicpConversationLogger
import com.justai.jaicf.context.manager.InMemoryBotContextManager
import com.justai.jaicf.context.manager.mongo.MongoBotContextManager
import com.justai.jaicf.logging.Slf4jConversationLogger
import com.justai.jaicf.template.scenario.MainScenario
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import java.util.*

private val contextManager = System.getenv("MONGODB_URI")?.let { url ->
    val uri = MongoClientURI(url)
    val client = MongoClient(uri)
    MongoBotContextManager(client.getDatabase(uri.database!!).getCollection("contexts"))
} ?: InMemoryBotContextManager

val accessToken: String = System.getenv("JAICP_API_TOKEN") ?: Properties().run {
    load(CailaNLUSettings::class.java.getResourceAsStream("/jaicp.properties"))
    getProperty("apiToken")
}

val cailaUrl: String = System.getenv("CAILA_URL") ?: Properties().run {
    load(CailaNLUSettings::class.java.getResourceAsStream("/jaicp.properties"))
    getProperty("cailaUrl")
}

private val cailaNLUSettings = CailaNLUSettings(
    cailaUrl = cailaUrl,
    accessToken = accessToken,
    confidenceThreshold = 0.2
)

val templateBot = BotEngine(
    model = MainScenario.model,
    defaultContextManager = contextManager,
    activators = arrayOf(
        CailaIntentActivator.Factory(cailaNLUSettings),
        RegexActivator
    ),
    conversationLoggers = arrayOf(
        Slf4jConversationLogger(),
        JaicpConversationLogger(accessToken, url = System.getenv("CA_URL") ?: DEFAULT_PROXY_URL)
    )
)

