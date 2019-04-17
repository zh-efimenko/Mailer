package io.zensoft.mailer.component

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.zensoft.mailer.model.exception.ExceptionResponse
import io.zensoft.mailer.property.AuthProperties
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthorizationFilter(
        private val properties: AuthProperties
) : Filter {

    override fun init(filterConfig: FilterConfig?) {}

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse

        if (request.requestURI.startsWith("/api")) {
            val token = request.getHeader(AUTHORIZATION)

            if (properties.token != token) {
                val exceptionResponse = ExceptionResponse(UNAUTHORIZED.value(), UNAUTHORIZED.reasonPhrase)
                response.status = exceptionResponse.status
                response.addHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                response.writer.write(jacksonObjectMapper().writeValueAsString(exceptionResponse))
                return
            }
        }

        chain.doFilter(request, response)
    }

    override fun destroy() {}

}
