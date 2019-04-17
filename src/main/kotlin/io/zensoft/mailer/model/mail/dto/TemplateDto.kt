package io.zensoft.mailer.model.mail.dto

import io.zensoft.mailer.model.mail.amqp.TemplateMessage
import io.zensoft.mailer.model.mail.http.TemplateRequest
import java.util.*

data class TemplateDto(
        var namespace: String,
        var name: String,
        var body: String
) {

    constructor(request: TemplateRequest) : this(
            request.namespace!!,
            request.template!!.originalFilename!!,
            String(request.template!!.bytes)
    )

    constructor(message: TemplateMessage) : this(
            message.namespace!!,
            message.name!!,
            String(Base64.getDecoder().decode(message.body!!))
    )

}
