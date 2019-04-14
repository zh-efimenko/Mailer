package io.zensoft.mailer.domain.mail

import io.zensoft.mailer.domain.mail.MailRequest.MailRecipientType.TO
import javax.validation.Valid
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotBlank
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
    private fun isMessage(): Boolean {
        return null != template || !message.isNullOrBlank()
    }


    data class MailFrom(
            @field:NotBlank var address: String? = null,
            var personal: String? = null
    )

    enum class MailRecipientType { TO, CC, BCC }

    data class MailTo(
            @field:NotBlank var address: String? = null,
            var recipientType: MailRecipientType = TO
    )

    data class MailAttachment(
            @field:NotBlank var name: String? = null,
            @field:NotBlank var content: String? = null
    )

    data class MailTemplate(
            @field:NotBlank var name: String? = null,
            var content: Map<String, String>? = null
    )

}
