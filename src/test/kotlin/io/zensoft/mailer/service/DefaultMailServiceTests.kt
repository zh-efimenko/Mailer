package io.zensoft.mailer.service

import freemarker.template.Configuration
import freemarker.template.Template
import io.zensoft.mailer.config.UnitTests
import io.zensoft.mailer.exception.MailSendException
import io.zensoft.mailer.model.mail.MailAttachment
import io.zensoft.mailer.model.mail.MailFrom
import io.zensoft.mailer.model.mail.MailTemplate
import io.zensoft.mailer.model.mail.MailTo
import io.zensoft.mailer.model.mail.dto.MailDto
import io.zensoft.mailer.property.StaticDataProperties
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.springframework.mail.javamail.JavaMailSender
import java.util.*
import javax.activation.DataHandler
import javax.mail.Session
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class DefaultMailServiceTests : UnitTests() {

    @Mock private lateinit var mailSender: JavaMailSender
    @Mock private lateinit var freeMarkerConfiguration: Configuration

    private val staticDataProperties = StaticDataProperties("file:./data/templates/")
    private val mimeMessage = MimeMessage(Session.getDefaultInstance(Properties()))

    private lateinit var mailService: MailService


    @Before
    fun setUp() {
        mailService = DefaultMailService(mailSender, freeMarkerConfiguration, staticDataProperties)

        given(mailSender.createMimeMessage()).willReturn(mimeMessage)
    }

    @Test
    fun sendTest() {
        val dto = createMailDto().apply { template = null; message = "message" }

        mailService.send(dto)

        verify(mailSender).send(mimeMessage)
    }

    @Test(expected = MailSendException::class)
    fun sendWhenTemplateIsNotFoundShouldThrowException() {
        val request = createMailDto()

        mailService.send(request)
    }

    @Test
    fun sendWhenAttachmentCorrectShouldSendMessageWithAttachment() {
        val dto = createMailDto().apply {
            attachments = setOf(MailAttachment("file.png", "YQ=="))
            template = null
            message = "message"
        }

        mailService.send(dto)

        verify(mailSender).send(mimeMessage)
    }

    @Test(expected = MailSendException::class)
    fun sendIfAttachmentIsNotCorrectShouldThrowException() {
        val dto = createMailDto().apply {
            attachments = setOf(MailAttachment("file.png", "1"))
            template = null
            message = "message"
        }

        mailService.send(dto)
    }

    @Test
    fun sendWhenTemplateContentExistsShouldSendMessageByTemplate() {
        val value = "SomeContent"
        val sourceTemplate = "<html><body>\${content}</body></html>"
        val expectedContent = sourceTemplate.replace("\${content}", value)

        val spy = spy(Configuration::class.java)
        val template = Template("template.ftl", sourceTemplate, spy)
        val captor = ArgumentCaptor.forClass(MimeMessage::class.java)

        val dto = createMailDto().apply {
            this.template = MailTemplate("Namespace", template.name, mapOf("content" to value))
        }

        given(freeMarkerConfiguration.getTemplate(template.name)).willReturn(template)
        doNothing().`when`(mailSender).send(captor.capture())

        mailService.send(dto)

        val actualContent = getContent(captor.value.dataHandler)

        assertEquals(expectedContent, actualContent)
    }

    @Test(expected = MailSendException::class)
    fun sendWhenTemplateContentNotExistsShouldThrowException() {
        val dto = createMailDto().apply { template = MailTemplate("template.ftl") }

        mailService.send(dto)
    }

    private fun getContent(dataHandler: DataHandler): String {
        val content = dataHandler.content
        return when (content) {
            is MimeMultipart -> getContent(content.getBodyPart(0).dataHandler)
            else -> content as String
        }
    }

    private fun createMailDto(): MailDto = MailDto(
            MailFrom("from@address.test"), setOf(MailTo("to@address.test")), "Subject",
            template = MailTemplate("Namespace", "Template"))

}
