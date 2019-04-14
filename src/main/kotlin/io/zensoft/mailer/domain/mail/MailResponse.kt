package io.zensoft.mailer.domain.mail

data class MailResponse(
        val status: Status
) {

    enum class Status { OK, FAIL }

}
