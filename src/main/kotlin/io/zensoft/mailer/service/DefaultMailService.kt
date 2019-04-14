package io.zensoft.mailer.service

import freemarker.template.Configuration
import io.zensoft.mailer.domain.mail.MailRequest
import io.zensoft.mailer.domain.mail.MailRequest.MailRecipientType
import io.zensoft.mailer.exception.MailSendException
import org.springframework.core.io.ByteArrayResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import java.util.*
import javax.annotation.PostConstruct

@Service
class DefaultMailService(
        private val sender: JavaMailSender,
        private val freeMarkerConfiguration: Configuration
) : MailService {

    @PostConstruct
    fun init() {
        freeMarkerConfiguration.logTemplateExceptions = false
    }

    override fun send(request: MailRequest) {
        val messageHelper = MimeMessageHelper(sender.createMimeMessage(), true)

        request.from?.let {
            messageHelper.setFrom(it.address!!, it.personal)
        }

        request.to.forEach {
            when (it.recipientType) {
                MailRecipientType.TO -> messageHelper.addTo(it.address!!)
                MailRecipientType.CC -> messageHelper.addCc(it.address!!)
                MailRecipientType.BCC -> messageHelper.addBcc(it.address!!)
            }
        }

        request.subject?.let {
            messageHelper.setSubject(it)
        }

        try {
            processMessage(request, messageHelper)

            request.attachments.forEach {
                messageHelper.addAttachment(it.name!!, ByteArrayResource(Base64.getDecoder().decode(it.content!!)))
            }

            sender.send(messageHelper.mimeMessage)
        } catch (e: Exception) {
            throw MailSendException(e.message)
        }
    }

    private fun processMessage(request: MailRequest, messageHelper: MimeMessageHelper) {
        if (request.template != null) {
            val template = freeMarkerConfiguration.getTemplate(request.template!!.name)
            val templateIntoString = FreeMarkerTemplateUtils.processTemplateIntoString(template, request.template!!.content)
            messageHelper.setText(templateIntoString, true)
        } else {
            messageHelper.setText(request.message!!, false)
        }
    }

}
