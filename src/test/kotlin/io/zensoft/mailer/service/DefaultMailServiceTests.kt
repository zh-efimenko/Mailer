package io.zensoft.mailer.service

import freemarker.template.Configuration
import freemarker.template.Template
import io.zensoft.mailer.config.UnitTests
import io.zensoft.mailer.domain.mail.MailRequest
import io.zensoft.mailer.domain.mail.MailRequest.*
import io.zensoft.mailer.exception.MailSendException
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

    private val mimeMessage = MimeMessage(Session.getDefaultInstance(Properties()))

    private lateinit var mailService: MailService


    @Before
    fun setUp() {
        mailService = DefaultMailService(mailSender, freeMarkerConfiguration)

        given(mailSender.createMimeMessage()).willReturn(mimeMessage)
    }

    @Test
    fun sendTest() {
        val request = createMailRequest().apply { template = null; message = "message" }

        mailService.send(request)

        verify(mailSender).send(mimeMessage)
    }

    @Test(expected = MailSendException::class)
    fun sendWhenTemplateIsNotFoundShouldThrowException() {
        val request = createMailRequest()

        mailService.send(request)
    }

    @Test
    fun sendWhenAttachmentCorrectShouldSendMessageWithAttachment() {
        val request = createMailRequest().apply {
            attachments = setOf(MailAttachment("file.png", "YQ=="))
            template = null
            message = "message"
        }

        mailService.send(request)

        verify(mailSender).send(mimeMessage)
    }

    @Test(expected = MailSendException::class)
    fun sendIfAttachmentIsNotCorrectShouldThrowException() {
        val request = createMailRequest().apply {
            attachments = setOf(MailAttachment("file.png", "1"))
            template = null
            message = "message"
        }

        mailService.send(request)
    }

    @Test
    fun sendWhenTemplateContentExistsShouldSendMessageByTemplate() {
        val value = "SomeContent"
        val sourceTemplate = "<html><body>\${content}</body></html>"
        val expectedContent = sourceTemplate.replace("\${content}", value)

        val spy = spy(Configuration::class.java)
        val template = Template("template.ftl", sourceTemplate, spy)
        val captor = ArgumentCaptor.forClass(MimeMessage::class.java)

        val request = createMailRequest().apply {
            this.template = MailTemplate(template.name, mapOf("content" to value))
        }

        given(freeMarkerConfiguration.getTemplate(template.name)).willReturn(template)
        doNothing().`when`(mailSender).send(captor.capture())

        mailService.send(request)

        val actualContent = getContent(captor.value.dataHandler)

        assertEquals(expectedContent, actualContent)
    }

    @Test(expected = MailSendException::class)
    fun sendWhenTemplateContentNotExistsShouldThrowException() {
        val request = createMailRequest().apply { template = MailTemplate("template.ftl") }

        mailService.send(request)
    }

    private fun getContent(dataHandler: DataHandler): String {
        val content = dataHandler.content
        return when (content) {
            is MimeMultipart -> getContent(content.getBodyPart(0).dataHandler)
            else -> content as String
        }
    }

    private fun createMailRequest(): MailRequest = MailRequest(
            MailFrom("from@address.test"), setOf(MailTo("to@address.test")), "Subject",
            template = MailTemplate("Template"))

}
