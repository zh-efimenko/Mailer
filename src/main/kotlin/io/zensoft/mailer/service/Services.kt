package io.zensoft.mailer.service

import io.zensoft.mailer.domain.mail.MailRequest
import org.springframework.web.multipart.MultipartFile

interface MailService {

    fun send(request: MailRequest)

}

interface TemplateService {

    fun add(file: MultipartFile): String

    fun update(file: MultipartFile)

    fun delete(templateName: String)

}
