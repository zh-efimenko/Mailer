package io.zensoft.mailer.annotation.validator

import io.zensoft.mailer.annotation.MultipartFileConstraint
import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class MultipartFileValidator : ConstraintValidator<MultipartFileConstraint, MultipartFile?> {

    private lateinit var constraint: MultipartFileConstraint


    override fun initialize(constraint: MultipartFileConstraint) {
        this.constraint = constraint
    }

    override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        value ?: return false

        if (value.size > constraint.maxSize) {
            return false
        }

        return constraint.contentTypeArray.any {
            MediaType.valueOf(it).isCompatibleWith(MediaType.valueOf(value.contentType!!))
        }
    }

}
