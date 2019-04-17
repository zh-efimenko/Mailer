package io.zensoft.mailer.model.mail.http

import io.zensoft.mailer.model.mail.MailAttachment
import io.zensoft.mailer.model.mail.MailFrom
import io.zensoft.mailer.model.mail.MailTemplate
import io.zensoft.mailer.model.mail.MailTo
import javax.validation.Valid
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotEmpty

data class MailRequest(
        @field:Valid var from: MailFrom? = null,
        @field:Valid @field:NotEmpty var to: Set<MailTo> = setOf(),
        var subject: String? = null,
        @field:Valid var template: MailTemplate? = null,
        var message: String? = null,
        @field:Valid var attachments: Set<MailAttachment> = setOf()
) {

    @AssertTrue(message = "Message or template is required")
    private fun isMessage(): Boolean = null != template || !message.isNullOrBlank()

}
