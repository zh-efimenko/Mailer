package io.zensoft.mailer.model.mail.amqp

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class TemplateMessage(
        @field:Pattern(regexp = "[a-zA-Z_]+") var namespace: String? = null,
        @field:NotBlank var name: String? = null,
        @field:NotBlank var body: String? = null
) : Message()
