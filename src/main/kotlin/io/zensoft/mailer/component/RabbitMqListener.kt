package io.zensoft.mailer.component

import io.zensoft.mailer.domain.mail.MailRequest
import io.zensoft.mailer.domain.mail.MailResponse
import io.zensoft.mailer.domain.mail.MailResponse.Status.FAIL
import io.zensoft.mailer.domain.mail.MailResponse.Status.OK
import io.zensoft.mailer.exception.MailException
import io.zensoft.mailer.service.MailService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import javax.validation.Valid

class RabbitMqListener(
        private val mailService: MailService
) {

    @RabbitListener(queues = ["\${rabbitmq.queue}"])
    fun listen(@Valid @Payload request: MailRequest): MailResponse {
        return try {
            mailService.send(request)
            MailResponse(OK)
        } catch (e: MailException) {
            log.error(e.message, e)
            MailResponse(FAIL)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(RabbitMqListener::class.java)
    }

}
