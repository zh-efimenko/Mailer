package io.zensoft.mailer.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ConfigurationProperties(prefix = "mail")
@Validated
@Component
data class MailProperties(

        /**
         * SMTP server host. For instance, `smtp.example.com`
         */
        @field:NotBlank var host: String? = null,

        /**
         * SMTP server port
         */
        @field:NotNull var port: Int? = null,

        /**
         * Login user of the SMTP server
         */
        @field:NotBlank var username: String? = null,

        /**
         * Login password of the SMTP server
         */
        @field:NotBlank var password: String? = null,

        /**
         * Protocol used by the SMTP server
         */
        @field:NotBlank var protocol: String? = null,

        /**
         * Default MimeMessage encoding
         */
        @field:NotBlank var defaultEncoding: String? = null,

        /**
         * Additional JavaMail Session properties
         */
        var properties: List<String> = listOf()

)