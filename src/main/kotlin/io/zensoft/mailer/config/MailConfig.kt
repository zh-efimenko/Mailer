package io.zensoft.mailer.config

import io.zensoft.mailer.property.MailProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.nio.charset.Charset
import java.util.*
import kotlin.text.Charsets.UTF_8

@Configuration
class MailConfig(
        private val mailProperties: MailProperties
) {

    @Bean
    fun mailSender(): JavaMailSender {
        val sender = JavaMailSenderImpl()
        applyProperties(sender)
        return sender
    }

    private fun applyProperties(sender: JavaMailSenderImpl) {
        sender.host = mailProperties.host!!
        sender.port = mailProperties.port!!
        sender.username = mailProperties.username!!
        sender.password = mailProperties.password!!
        sender.protocol = mailProperties.protocol!!
        sender.defaultEncoding = getEncoding()

        if (mailProperties.properties.isNotEmpty()) {
            val properties = mailProperties.properties.map { p ->
                val property = p.split("=")
                property[0] to property[1]
            }.toMap()

            sender.javaMailProperties = asProperties(properties)
        }
    }

    private fun getEncoding(): String {
        val defaultEncoding = try {
            Charset.forName(mailProperties.defaultEncoding)
        } catch (e: Exception) {
            UTF_8
        }

        return defaultEncoding.name()
    }

    private fun asProperties(source: Map<String, String>): Properties {
        val properties = Properties()
        properties.putAll(source)
        return properties
    }

}