package io.zensoft.mailer.service

import io.zensoft.mailer.model.mail.dto.MailDto
import io.zensoft.mailer.model.mail.dto.TemplateDto

interface MailService {

    fun send(dto: MailDto)

}

interface TemplateService {

    fun addOrUpdate(dto: TemplateDto)

}
