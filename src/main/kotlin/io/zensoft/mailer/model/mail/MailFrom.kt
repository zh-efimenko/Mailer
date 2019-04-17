package io.zensoft.mailer.model.mail

import javax.validation.constraints.NotBlank

data class MailFrom(
        @field:NotBlank var address: String? = null,
        var personal: String? = null
)
