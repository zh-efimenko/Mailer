package io.zensoft.mailer.model.mail

import javax.validation.constraints.NotBlank

data class MailAttachment(
        @field:NotBlank var name: String? = null,
        @field:NotBlank var content: String? = null
)
