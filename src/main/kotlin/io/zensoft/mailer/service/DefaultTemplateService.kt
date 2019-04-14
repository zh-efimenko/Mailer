package io.zensoft.mailer.service

import io.zensoft.mailer.exception.DuplicateTemplateException
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.PostConstruct

@Service
class DefaultTemplateService(
        freeMarkerProperties: FreeMarkerProperties
) : TemplateService {

    private val templateDirectory: Path by lazy {
        val path = freeMarkerProperties.templateLoaderPath.first().removePrefix("file:")
        Paths.get(path)
    }


    @PostConstruct
    private fun init() {
        if (!Files.exists(templateDirectory)) {
            Files.createDirectories(templateDirectory)
        }
    }

    override fun add(file: MultipartFile): String {
        val path = Paths.get("$templateDirectory/${file.originalFilename!!}")
        if (Files.exists(path)) {
            throw DuplicateTemplateException("Template ${file.originalFilename!!} already exists!")
        }

        save(file)
        return file.originalFilename!!
    }

    override fun update(file: MultipartFile) {
        delete(file.originalFilename!!)
        save(file)
    }

    override fun delete(templateName: String) {
        val path = Paths.get("$templateDirectory/$templateName")
        if (Files.exists(path)) {
            Files.delete(path)
        } else {
            throw FileNotFoundException("Template $templateName is not found!")
        }
    }

    private fun save(file: MultipartFile) {
        val path = Paths.get("$templateDirectory/${file.originalFilename!!}")
        Files.write(path, file.bytes)
    }

}
