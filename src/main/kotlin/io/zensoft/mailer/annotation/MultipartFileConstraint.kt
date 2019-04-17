package io.zensoft.mailer.annotation

import io.zensoft.mailer.annotation.validator.MultipartFileValidator
import org.springframework.http.MediaType.ALL_VALUE
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(value = AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MultipartFileValidator::class])
annotation class MultipartFileConstraint(
        val message: String = "File size or Content-Type is invalid",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = [],
        val maxSize: Long = 1 * 1024 * 1024,
        val contentTypeArray: Array<String> = [ALL_VALUE]
)
