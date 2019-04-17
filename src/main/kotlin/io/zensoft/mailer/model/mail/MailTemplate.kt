package io.zensoft.mailer.model.mail

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class MailTemplate(
        @field:Pattern(regexp = "[a-zA-Z_]+") var namespace: String? = null,
        @field:NotBlank var name: String? = null,
        var content: Map<String, String>? = null
)
