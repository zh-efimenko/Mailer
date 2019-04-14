package io.zensoft.mailer.component

import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

class RabbitMqCondition : Condition {

    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean =
            context.environment.getProperty("rabbitmq.enable")!!.toBoolean()

}