package io.zensoft.mailer.config.swagger.plugin

import com.google.common.annotations.VisibleForTesting
import com.google.common.base.Optional
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.bean.validators.plugins.Validators.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin
import springfox.documentation.spi.schema.contexts.ModelPropertyContext
import javax.validation.constraints.Pattern

@Component
@Order(BEAN_VALIDATOR_PLUGIN_ORDER)
class PatternAnnotationPlugin : ModelPropertyBuilderPlugin {

    /**
     * support all documentationTypes
     */
    override fun supports(delimiter: DocumentationType): Boolean = true

    /**
     * read Pattern annotation
     */
    override fun apply(context: ModelPropertyContext) {
        val pattern = extractAnnotation(context)
        if (pattern.isPresent) {
            context.builder.required(pattern.isPresent)
        }
    }

    @VisibleForTesting
    internal fun extractAnnotation(context: ModelPropertyContext): Optional<Pattern> {
        return annotationFromBean<Pattern>(context, Pattern::class.java)
                .or(annotationFromField<Pattern>(context, Pattern::class.java))
    }

}
