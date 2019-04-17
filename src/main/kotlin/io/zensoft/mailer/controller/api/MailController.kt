package io.zensoft.mailer.controller.api

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import io.zensoft.mailer.model.mail.dto.MailDto
import io.zensoft.mailer.model.mail.dto.TemplateDto
import io.zensoft.mailer.model.mail.http.MailRequest
import io.zensoft.mailer.model.mail.http.TemplateRequest
import io.zensoft.mailer.service.MailService
import io.zensoft.mailer.service.TemplateService
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/mail")
class MailController(
        private val mailService: MailService,
        private val templateService: TemplateService
) {

    @PostMapping("/doSend")
    @ApiOperation("Send mail")
    @ApiImplicitParam(name = AUTHORIZATION, required = true, dataType = "string", paramType = "header")
    fun sendMail(@Valid @RequestBody request: MailRequest) {
        mailService.send(MailDto(request))
    }

    @PostMapping("/templates/doAddOrUpdate")
    @ApiOperation(value = "Add or update template")
    @ApiImplicitParam(name = AUTHORIZATION, required = true, dataType = "string", paramType = "header")
    fun addTemplate(@Valid @ModelAttribute request: TemplateRequest) {
        templateService.addOrUpdate(TemplateDto(request))
    }

}
