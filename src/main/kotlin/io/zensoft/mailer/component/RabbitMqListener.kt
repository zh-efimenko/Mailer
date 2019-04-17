package io.zensoft.mailer.component

import io.zensoft.mailer.model.mail.amqp.*
import io.zensoft.mailer.model.mail.amqp.Response.Status.FAIL
import io.zensoft.mailer.model.mail.amqp.Response.Status.OK
import io.zensoft.mailer.model.mail.dto.MailDto
import io.zensoft.mailer.model.mail.dto.TemplateDto
import io.zensoft.mailer.service.MailService
import io.zensoft.mailer.service.TemplateService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import javax.validation.Valid

class RabbitMqListener(
        private val mailService: MailService,
        private val templateService: TemplateService
) {

    @RabbitListener(queues = ["\${rabbitmq.queue}"])
    fun listen(@Valid @Payload message: Message): Response {
        return try {
            when (message) {
                is MailMessage -> mailService.send(MailDto(message))
                is MailTemplateMessage -> mailService.send(MailDto(message))
                is TemplateMessage -> templateService.addOrUpdate(TemplateDto(message))
            }

            Response(OK)
        } catch (e: Exception) {
            log.error(e.message, e)
            Response(FAIL, e.message)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(RabbitMqListener::class.java)
    }

}
