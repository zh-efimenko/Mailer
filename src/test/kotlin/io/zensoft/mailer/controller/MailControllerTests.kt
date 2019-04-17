package io.zensoft.mailer.controller

import io.zensoft.mailer.config.ControllerTests
import io.zensoft.mailer.controller.api.MailController
import io.zensoft.mailer.model.mail.MailTo
import io.zensoft.mailer.model.mail.dto.MailDto
import io.zensoft.mailer.model.mail.dto.TemplateDto
import io.zensoft.mailer.model.mail.http.MailRequest
import io.zensoft.mailer.model.mail.http.TemplateRequest
import io.zensoft.mailer.service.MailService
import io.zensoft.mailer.service.TemplateService
import org.junit.Test
import org.mockito.Mockito.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_HTML_VALUE
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MailController::class)
class MailControllerTests : ControllerTests() {

    @MockBean
    private lateinit var mailService: MailService

    @MockBean
    private lateinit var templateService: TemplateService


    @Test
    fun sendMailTest() {
        val request = MailRequest(to = setOf(MailTo("to@address.test")), message = "message")

        mvc.perform(post("/api/mail/doSend")
                .header(AUTHORIZATION, authProperties.token)
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "to": [
                        {
                          "address": "${request.to.first().address}",
                          "recipientType": "${request.to.first().recipientType.name}"
                        }
                      ],
                      "message": "${request.message}"
                    }
                """.trimMargin()))

                .andExpect(status().isOk)

        verify(mailService).send(MailDto(request))
    }

    @Test
    fun sendMailIfRequestIsNotValidShouldThrowException() {
        mvc.perform(post("/api/mail/doSend")
                .header(AUTHORIZATION, authProperties.token)
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "message": "Message"
                    }
                """.trimMargin()))

                .andExpect(status().isBadRequest)
                .andExpect(content().json("""
                    {
                      "status": ${BAD_REQUEST.value()},
                      "message": "${BAD_REQUEST.reasonPhrase}",
                      "errors": [
                        {
                          "code": "NotEmpty",
                          "field": "to",
                          "message": "must not be empty"
                        }
                      ]
                    }
                """.trimIndent(), true))
    }

    @Test
    fun sendMailIfRecipientTypeIsNotCorrectShouldThrowException() {
        mvc.perform(post("/api/mail/doSend")
                .header(AUTHORIZATION, authProperties.token)
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "to": [
                        {
                          "address": "Address",
                          "recipientType": "TOO"
                        }
                      ],
                      "message": "Message"
                    }
                """.trimMargin()))

                .andExpect(status().isBadRequest)
    }

    @Test
    fun addOrUpdateTemplateTest() {
        val multipartFile = MockMultipartFile("template", "template.ftl", TEXT_HTML_VALUE,
                "test".toByteArray())
        val request = TemplateRequest("namespace", multipartFile)

        mvc.perform(multipart("/api/mail/templates/doAddOrUpdate")
                .file(request.template as MockMultipartFile)
                .param("namespace", request.namespace)
                .header(AUTHORIZATION, authProperties.token))

                .andExpect(status().isOk)

        verify(templateService).addOrUpdate(TemplateDto(request))
    }

}
