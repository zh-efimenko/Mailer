package io.zensoft.mailer.model.mail.amqp

import com.fasterxml.jackson.annotation.JsonProperty

data class Response(
        @JsonProperty(value = "status") val status: Status,
        @JsonProperty(value = "message") val message: String? = null
) {

    enum class Status { OK, FAIL }

}
