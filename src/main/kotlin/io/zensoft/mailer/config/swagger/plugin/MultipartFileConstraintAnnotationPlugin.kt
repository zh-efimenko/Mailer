package io.zensoft.mailer.config.swagger.plugin

import com.google.common.annotations.VisibleForTesting
import com.google.common.base.Optional
import io.zensoft.mailer.annotation.MultipartFileConstraint
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.bean.validators.plugins.Validators.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin
import springfox.documentation.spi.schema.contexts.ModelPropertyContext

@Component
@Order(BEAN_VALIDATOR_PLUGIN_ORDER)
class MultipartFileConstraintAnnotationPlugin : ModelPropertyBuilderPlugin {

    /**
     * support all documentationTypes
     */
    override fun supports(delimiter: DocumentationType): Boolean = true

    /**
     * read MultipartFileConstraint annotation
     */
    override fun apply(context: ModelPropertyContext) {
        val multipartFileConstraint = extractAnnotation(context)
        if (multipartFileConstraint.isPresent) {
            context.builder.required(multipartFileConstraint.isPresent)
        }
    }

    @VisibleForTesting
    internal fun extractAnnotation(context: ModelPropertyContext): Optional<MultipartFileConstraint> {
        return annotationFromBean<MultipartFileConstraint>(context, MultipartFileConstraint::class.java)
                .or(annotationFromField<MultipartFileConstraint>(context, MultipartFileConstraint::class.java))
    }

}
