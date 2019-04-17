package io.zensoft.mailer.model.mail.amqp

import io.zensoft.mailer.model.mail.MailAttachment
import io.zensoft.mailer.model.mail.MailFrom
import io.zensoft.mailer.model.mail.MailTemplate
import io.zensoft.mailer.model.mail.MailTo
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class MailTemplateMessage(
        @field:Valid var from: MailFrom? = null,
        @field:Valid @field:NotEmpty var to: Set<MailTo> = setOf(),
        var subject: String? = null,
        @field:Valid @field:NotNull var template: MailTemplate? = null,
        @field:Valid var attachments: Set<MailAttachment> = setOf()
) : Message()
