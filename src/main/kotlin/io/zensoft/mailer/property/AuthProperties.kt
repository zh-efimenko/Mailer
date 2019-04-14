package io.zensoft.mailer.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "auth")
@Validated
@Component
data class AuthProperties(

        /**
         * Authorization token. Lets the application to access to your "Mailer".
         * Example: Authorization: YOUR_TOKEN
         */
        @field:NotBlank var token: String? = null

)
