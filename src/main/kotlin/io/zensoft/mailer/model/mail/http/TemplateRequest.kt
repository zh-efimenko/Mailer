package io.zensoft.mailer.model.mail.http

import io.zensoft.mailer.annotation.MultipartFileConstraint
import org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE
import org.springframework.http.MediaType.TEXT_HTML_VALUE
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.Pattern

data class TemplateRequest(

        @field:Pattern(regexp = "[a-zA-Z_]+") var namespace: String? = null,

        @field:MultipartFileConstraint(contentTypeArray = [TEXT_HTML_VALUE, APPLICATION_OCTET_STREAM_VALUE])
        var template: MultipartFile? = null

)
