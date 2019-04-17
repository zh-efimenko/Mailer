package io.zensoft.mailer.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "rabbitmq")
@Validated
data class RabbitMqProperties(

        /**
         * The name of RabbitMQ exchanger
         */
        @field:NotBlank var exchanger: String? = null,

        /**
         * The name of RabbitMQ queue
         */
        @field:NotBlank var queue: String? = null,

        /**
         * The name of RabbitMQ routing-key
         */
        @field:NotBlank var routingKey: String? = null

)
