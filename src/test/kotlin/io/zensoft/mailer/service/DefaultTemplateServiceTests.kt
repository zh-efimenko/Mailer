package io.zensoft.mailer.service

import io.zensoft.mailer.model.mail.dto.TemplateDto
import io.zensoft.mailer.property.StaticDataProperties
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
class DefaultTemplateServiceTests {

    @Autowired
    private lateinit var staticDataProperties: StaticDataProperties

    @Autowired
    private lateinit var templateService: TemplateService

    private val pathToNamespace: Path by lazy {
        Paths.get("${staticDataProperties.getTemplateLoaderPathWithoutSchema()}/$NAMESPACE")
    }
    private val pathToTemplate: Path by lazy {
        Paths.get("$pathToNamespace/$FILE_NAME")
    }


    @After
    fun clean() {
        Files.deleteIfExists(pathToTemplate)
        Files.deleteIfExists(pathToNamespace)
    }

    @Test
    fun addOrUpdateTest() {
        val dto = TemplateDto(NAMESPACE, FILE_NAME, String(Base64.getEncoder().encode("content".toByteArray())))

        templateService.addOrUpdate(dto)

        assertTrue(Files.exists(pathToNamespace))
        assertTrue(Files.exists(pathToTemplate))
    }

    companion object {
        private const val FILE_NAME = "template.ftl"
        private const val NAMESPACE = "namespace"
    }

}
