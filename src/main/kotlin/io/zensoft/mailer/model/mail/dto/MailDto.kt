package io.zensoft.mailer.model.mail.dto

import io.zensoft.mailer.model.mail.MailAttachment
import io.zensoft.mailer.model.mail.MailFrom
import io.zensoft.mailer.model.mail.MailTemplate
import io.zensoft.mailer.model.mail.MailTo
import io.zensoft.mailer.model.mail.amqp.MailMessage
import io.zensoft.mailer.model.mail.amqp.MailTemplateMessage
import io.zensoft.mailer.model.mail.http.MailRequest

data class MailDto(
        var from: MailFrom? = null,
        var to: Set<MailTo> = setOf(),
        var subject: String? = null,
        var template: MailTemplate? = null,
        var message: String? = null,
        var attachments: Set<MailAttachment> = setOf()
) {

    constructor(request: MailRequest) : this(
            request.from,
            request.to,
            request.subject,
            request.template,
            request.message,
            request.attachments
    )

    constructor(message: MailMessage) : this(
            message.from,
            message.to,
            message.subject,
            message = message.message,
            attachments = message.attachments
    )

    constructor(message: MailTemplateMessage) : this(
            message.from,
            message.to,
            message.subject,
            message.template,
            attachments = message.attachments
    )

}
