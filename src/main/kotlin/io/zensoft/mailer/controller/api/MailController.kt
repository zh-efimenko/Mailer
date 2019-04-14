package io.zensoft.mailer.controller.api

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import io.zensoft.mailer.domain.mail.MailRequest
import io.zensoft.mailer.service.MailService
import io.zensoft.mailer.service.TemplateService
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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
        mailService.send(request)
    }

    @PostMapping("/templates")
    @ApiOperation(value = "Add template")
    @ApiImplicitParam(name = AUTHORIZATION, required = true, dataType = "string", paramType = "header")
    fun addTemplate(@RequestPart("template") template: MultipartFile): String =
            templateService.add(template)

    @PutMapping("/templates")
    @ApiOperation(value = "Update template")
    @ApiImplicitParam(name = AUTHORIZATION, required = true, dataType = "string", paramType = "header")
    fun updateTemplate(@RequestPart("template") template: MultipartFile) {
        templateService.update(template)
    }

    @DeleteMapping("/templates/{templateName:.*\\..*}")
    @ApiOperation(value = "Delete template")
    @ApiImplicitParam(name = AUTHORIZATION, required = true, dataType = "string", paramType = "header")
    fun deleteTemplate(@PathVariable templateName: String) {
        templateService.delete(templateName)
    }

}
