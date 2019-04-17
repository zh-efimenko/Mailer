package io.zensoft.mailer.service

import io.zensoft.mailer.model.mail.dto.TemplateDto
import io.zensoft.mailer.property.StaticDataProperties
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.PostConstruct

@Service
class DefaultTemplateService(
        staticDataProperties: StaticDataProperties
) : TemplateService {

    private val templateDirectory: Path by lazy {
        Paths.get(staticDataProperties.getTemplateLoaderPathWithoutSchema())
    }


    @PostConstruct
    private fun init() {
        if (!Files.exists(templateDirectory)) {
            Files.createDirectories(templateDirectory)
        }
    }

    override fun addOrUpdate(dto: TemplateDto) {
        val namespaceDirectory = Paths.get("$templateDirectory/${dto.namespace}")
        if (!Files.exists(namespaceDirectory)) {
            Files.createDirectories(namespaceDirectory)
        }

        val path = Paths.get("$namespaceDirectory/${dto.name}")
        Files.deleteIfExists(path)
        Files.write(path, dto.body.toByteArray())
    }

}
