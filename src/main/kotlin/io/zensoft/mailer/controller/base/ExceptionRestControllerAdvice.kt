package io.zensoft.mailer.controller.base

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.zensoft.mailer.exception.MailSendException
import io.zensoft.mailer.model.exception.ErrorDto
import io.zensoft.mailer.model.exception.ExceptionResponse
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MultipartException

@RestControllerAdvice
class ExceptionRestControllerAdvice {

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    fun bindExceptionHandler(exception: BindException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), BAD_REQUEST.reasonPhrase,
                    exception.bindingResult.allErrors.map { ErrorDto(it) })

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(exception: MethodArgumentNotValidException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), BAD_REQUEST.reasonPhrase,
                    exception.bindingResult.allErrors.map { ErrorDto(it) })

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MailSendException::class)
    fun mailExceptionHandler(exception: MailSendException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.localizedMessage)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MultipartException::class)
    fun multipartExceptionHandler(exception: MultipartException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException::class)
    fun invalidFormatExceptionHandler(exception: InvalidFormatException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.localizedMessage)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MismatchedInputException::class)
    fun mismatchedInputExceptionHandler(exception: MismatchedInputException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MissingKotlinParameterException::class)
    fun missingKotlinParameterExceptionHandler(exception: MissingKotlinParameterException): ExceptionResponse =
            ExceptionResponse(BAD_REQUEST.value(), exception.message!!)

    @ResponseStatus(code = INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateExceptionHandler(exception: IllegalStateException): ExceptionResponse =
            ExceptionResponse(INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR.reasonPhrase)

}
