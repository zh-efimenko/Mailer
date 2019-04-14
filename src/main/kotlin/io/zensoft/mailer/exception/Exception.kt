package io.zensoft.mailer.exception

open class MailException(message: String?) : RuntimeException(message)

class MailSendException(message: String?) : MailException(message)

class DuplicateTemplateException(message: String?) : MailException(message)
