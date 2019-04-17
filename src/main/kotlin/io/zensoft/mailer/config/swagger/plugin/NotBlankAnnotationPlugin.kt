package io.zensoft.mailer.config.swagger.plugin

import com.google.common.annotations.VisibleForTesting
import com.google.common.base.Optional
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.bean.validators.plugins.Validators
import springfox.bean.validators.plugins.Validators.annotationFromBean
import springfox.bean.validators.plugins.Validators.annotationFromField
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin
import springfox.documentation.spi.schema.contexts.ModelPropertyContext
import javax.validation.constraints.NotBlank

@Component
@Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
class NotBlankAnnotationPlugin : ModelPropertyBuilderPlugin {

    /**
     * support all documentationTypes
     */
    override fun supports(delimiter: DocumentationType): Boolean = true

    /**
     * read NotBlank annotation
     */
    override fun apply(context: ModelPropertyContext) {
        val notBlank = extractAnnotation(context)
        if (notBlank.isPresent) {
            context.builder.required(notBlank.isPresent)
        }
    }

    @VisibleForTesting
    internal fun extractAnnotation(context: ModelPropertyContext): Optional<NotBlank> {
        return annotationFromBean<NotBlank>(context, NotBlank::class.java)
                .or(annotationFromField<NotBlank>(context, NotBlank::class.java))
    }

}
