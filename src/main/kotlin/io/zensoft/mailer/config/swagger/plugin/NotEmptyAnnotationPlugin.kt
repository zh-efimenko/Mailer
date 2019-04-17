package io.zensoft.mailer.config.swagger.plugin

import com.google.common.annotations.VisibleForTesting
import com.google.common.base.Optional
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.bean.validators.plugins.Validators.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin
import springfox.documentation.spi.schema.contexts.ModelPropertyContext
import javax.validation.constraints.NotEmpty

@Component
@Order(BEAN_VALIDATOR_PLUGIN_ORDER)
class NotEmptyAnnotationPlugin : ModelPropertyBuilderPlugin {

    /**
     * support all documentationTypes
     */
    override fun supports(delimiter: DocumentationType): Boolean = true

    /**
     * read NotEmpty annotation
     */
    override fun apply(context: ModelPropertyContext) {
        val notEmpty = extractAnnotation(context)
        if (notEmpty.isPresent) {
            context.builder.required(notEmpty.isPresent)
        }
    }

    @VisibleForTesting
    internal fun extractAnnotation(context: ModelPropertyContext): Optional<NotEmpty> {
        return annotationFromBean<NotEmpty>(context, NotEmpty::class.java)
                .or(annotationFromField<NotEmpty>(context, NotEmpty::class.java))
    }

}
