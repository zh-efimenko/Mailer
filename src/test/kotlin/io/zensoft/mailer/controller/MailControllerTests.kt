package io.zensoft.mailer.controller

import io.zensoft.mailer.config.ControllerTests
import io.zensoft.mailer.controller.api.MailController
import io.zensoft.mailer.domain.mail.MailRequest
import io.zensoft.mailer.domain.mail.MailRequest.MailTo
import io.zensoft.mailer.exception.DuplicateTemplateException
import io.zensoft.mailer.service.MailService
import io.zensoft.mailer.service.TemplateService
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.MULTIPART_FORM_DATA
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
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

        verify(mailService).send(request)
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
    fun addTemplateTest() {
        val multipartFile = MockMultipartFile("template", "template.ftl", MULTIPART_FORM_DATA.type,
                "test".toByteArray())

        mvc.perform(multipart("/api/mail/templates")
                .file(multipartFile)
                .header(AUTHORIZATION, authProperties.token))

                .andExpect(status().isOk)

        verify(templateService).add(multipartFile)
    }

    @Test
    fun addTemplateIfTemplateAlreadyExistsShouldThrowException() {
        val multipartFile = MockMultipartFile("template", "template.ftl", MULTIPART_FORM_DATA.type,
                "test".toByteArray())
        val message = "Template ${multipartFile.originalFilename} already exists!"

        given(templateService.add(multipartFile)).willThrow(DuplicateTemplateException(message))

        mvc.perform(multipart("/api/mail/templates")
                .file(multipartFile)
                .header(AUTHORIZATION, authProperties.token))

                .andExpect(status().isBadRequest)
                .andExpect(content().json("""
                    {
                      "status": ${BAD_REQUEST.value()},
                      "message": "$message"
                    }
                """.trimIndent(), true))
    }

    @Test
    fun updateTemplateTest() {
        val multipartFile = MockMultipartFile("template", "template.ftl", MULTIPART_FORM_DATA.type,
                "test".toByteArray())

        mvc.perform(multipart("/api/mail/templates")
                .file(multipartFile)
                .with { request -> request.apply { method = PUT.name } }
                .header(AUTHORIZATION, authProperties.token))

                .andExpect(status().isOk)

        verify(templateService).update(multipartFile)
    }

    @Test
    fun deleteTemplateTest() {
        val fileName = "template.ftl"

        mvc.perform(delete("/api/mail/templates/$fileName")
                .header(AUTHORIZATION, authProperties.token))

                .andExpect(status().isOk)

        verify(templateService).delete(fileName)
    }

}
