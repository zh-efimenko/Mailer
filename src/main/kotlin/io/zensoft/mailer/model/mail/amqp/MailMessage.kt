package io.zensoft.mailer.model.mail.amqp

import io.zensoft.mailer.model.mail.MailAttachment
import io.zensoft.mailer.model.mail.MailFrom
import io.zensoft.mailer.model.mail.MailTo
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class MailMessage(
        @field:Valid var from: MailFrom? = null,
        @field:Valid @field:NotEmpty var to: Set<MailTo> = setOf(),
        var subject: String? = null,
        @field:NotBlank var message: String? = null,
        @field:Valid var attachments: Set<MailAttachment> = setOf()
) : Message()
