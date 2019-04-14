package io.zensoft.mailer.domain.exception

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ExceptionResponse(
        val status: Int,
        val message: String,
        val errors: List<ErrorDto> = emptyList()
)
