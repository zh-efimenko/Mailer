package io.zensoft.mailer.config

import io.zensoft.mailer.component.RabbitMqCondition
import io.zensoft.mailer.component.RabbitMqListener
import io.zensoft.mailer.property.RabbitMqProperties
import io.zensoft.mailer.service.MailService
import io.zensoft.mailer.service.TemplateService
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory
import org.springframework.validation.Validator

@Configuration
@Conditional(RabbitMqCondition::class)
@EnableConfigurationProperties(RabbitMqProperties::class)
class RabbitMqConfig(
        @Qualifier("mvcValidator") private val validator: Validator,
        private val properties: RabbitMqProperties,
        private val mailService: MailService,
        private val templateService: TemplateService
) : RabbitListenerConfigurer {

    @Bean
    fun directExchange(amqpAdmin: AmqpAdmin): DirectExchange {
        val exchange = DirectExchange(properties.exchanger, true, false)
        amqpAdmin.declareExchange(exchange)
        return exchange
    }

    @Bean
    fun queue(amqpAdmin: AmqpAdmin): Queue {
        val queue = Queue(properties.queue, true, false, false)
        amqpAdmin.declareQueue(queue)
        return queue
    }

    @Bean
    fun binding(queue: Queue, directExchange: DirectExchange, amqpAdmin: AmqpAdmin): Binding {
        val binding = BindingBuilder.bind(queue).to(directExchange).with(properties.routingKey)
        amqpAdmin.declareBinding(binding)
        return binding
    }

    /**
     * Configuration of rabbit listener in order to use "@Valid message"
     */
    override fun configureRabbitListeners(registrar: RabbitListenerEndpointRegistrar) {
        registrar.messageHandlerMethodFactory = handlerMethodFactory()
    }

    /**
     * Configuration of rabbit listener container factory for convert message to json
     */
    @Bean
    fun rabbitListenerContainerFactory(connectionFactory: ConnectionFactory,
                                       converter: Jackson2JsonMessageConverter): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setMessageConverter(converter)
        factory.setErrorHandler { throw AmqpRejectAndDontRequeueException(it.message, it) }
        return factory
    }

    @Bean
    fun converter(): Jackson2JsonMessageConverter = Jackson2JsonMessageConverter()

    @Bean
    fun handlerMethodFactory(): DefaultMessageHandlerMethodFactory {
        val factory = DefaultMessageHandlerMethodFactory()
        factory.setValidator(validator)
        return factory
    }

    @Bean
    fun rabbitMqListener(): RabbitMqListener = RabbitMqListener(mailService, templateService)

}
