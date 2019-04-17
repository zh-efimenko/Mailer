package io.zensoft.mailer.model.mail.amqp

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
        Type(value = MailMessage::class, name = "mail-message"),
        Type(value = MailTemplateMessage::class, name = "mail-template"),
        Type(value = TemplateMessage::class, name = "template")
)
abstract class Message
