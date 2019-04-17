package io.zensoft.mailer.model.mail

import io.zensoft.mailer.model.mail.MailTo.MailRecipientType.TO
import javax.validation.constraints.NotBlank

data class MailTo(
        @field:NotBlank var address: String? = null,
        var recipientType: MailRecipientType = TO
) {

    enum class MailRecipientType { TO, CC, BCC }

}
