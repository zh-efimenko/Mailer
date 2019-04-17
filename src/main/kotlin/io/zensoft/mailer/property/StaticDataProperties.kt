package io.zensoft.mailer.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Pattern

@ConfigurationProperties(prefix = "static-data")
@Validated
@Component
data class StaticDataProperties(

        /**
         * Template location must end in slash '/'
         */
        @field:Pattern(regexp = "file:.*/$") var templateLoaderPath: String? = null

) {

    fun getTemplateLoaderPathWithoutSchema() = templateLoaderPath!!.removePrefix("file:")

}
