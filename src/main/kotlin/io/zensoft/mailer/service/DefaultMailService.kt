package io.zensoft.mailer.service

import freemarker.template.Configuration
import io.zensoft.mailer.exception.MailSendException
import io.zensoft.mailer.model.mail.MailTo.MailRecipientType.*
import io.zensoft.mailer.model.mail.dto.MailDto
import io.zensoft.mailer.property.StaticDataProperties
import org.springframework.core.io.ByteArrayResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import java.nio.file.Paths
import java.util.*
import javax.annotation.PostConstruct

@Service
class DefaultMailService(
        private val sender: JavaMailSender,
        private val freeMarkerConfiguration: Configuration,
        private val staticDataProperties: StaticDataProperties
) : MailService {

    @PostConstruct
    fun init() {
        freeMarkerConfiguration.logTemplateExceptions = false
    }

    override fun send(dto: MailDto) {
        val messageHelper = MimeMessageHelper(sender.createMimeMessage(), true)

        dto.from?.let {
            it.personal?.let { personal ->
                messageHelper.setFrom(it.address!!, personal)
            } ?: messageHelper.setFrom(it.address!!)
        }

        dto.to.forEach {
            when (it.recipientType) {
                TO -> messageHelper.addTo(it.address!!)
                CC -> messageHelper.addCc(it.address!!)
                BCC -> messageHelper.addBcc(it.address!!)
            }
        }

        dto.subject?.let {
            messageHelper.setSubject(it)
        }

        try {
            processMessage(dto, messageHelper)

            dto.attachments.forEach {
                messageHelper.addAttachment(it.name!!, ByteArrayResource(Base64.getDecoder().decode(it.content!!)))
            }

            sender.send(messageHelper.mimeMessage)
        } catch (e: Exception) {
            throw MailSendException(e.message)
        }
    }

    private fun processMessage(dto: MailDto, messageHelper: MimeMessageHelper) {
        dto.template?.let {
            val templateLoaderPath = staticDataProperties.getTemplateLoaderPathWithoutSchema()
            val templatePath = Paths.get("$templateLoaderPath/${dto.template!!.namespace}")

            freeMarkerConfiguration.setDirectoryForTemplateLoading(templatePath.toFile())
            val template = freeMarkerConfiguration.getTemplate(dto.template!!.name)

            val templateIntoString = FreeMarkerTemplateUtils.processTemplateIntoString(template, dto.template!!.content as Any)

            messageHelper.setText(templateIntoString, true)
        } ?: messageHelper.setText(dto.message!!, false)
    }

}
