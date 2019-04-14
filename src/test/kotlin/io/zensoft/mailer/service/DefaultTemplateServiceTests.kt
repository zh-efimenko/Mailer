package io.zensoft.mailer.service

import io.zensoft.mailer.exception.DuplicateTemplateException
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.MULTIPART_FORM_DATA
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
class DefaultTemplateServiceTests {

    @Autowired
    private lateinit var freeMarkerProperties: FreeMarkerProperties

    @Autowired
    private lateinit var templateService: TemplateService

    private val pathToTemplate: Path by lazy {
        Paths.get("${freeMarkerProperties.templateLoaderPath.first().removePrefix("file:")}/$FILE_NAME")
    }


    @After
    fun clean() {
        Files.deleteIfExists(pathToTemplate)
    }

    @Test
    fun addTest() {
        val multipartFile = createMultiPartFile()
        val result = templateService.add(multipartFile)

        assertTrue(Files.exists(pathToTemplate))
        assertEquals(FILE_NAME, result)
    }

    @Test(expected = DuplicateTemplateException::class)
    fun addWhenTemplateExistsShouldThrowException() {
        Files.createFile(pathToTemplate)
        val multipartFile = createMultiPartFile()
        templateService.add(multipartFile)
    }

    @Test
    fun updateTest() {
        Files.createFile(pathToTemplate)
        val beforeUpdate = Files.readAllBytes(pathToTemplate)
        val multipartFile = createMultiPartFile()

        templateService.update(multipartFile)

        assertNotSame(beforeUpdate, Files.readAllBytes(pathToTemplate))
    }

    @Test(expected = FileNotFoundException::class)
    fun updateWhenTemplateNotExistsShouldThrowException() {
        templateService.update(createMultiPartFile())
    }

    @Test
    fun deleteTest() {
        Files.createFile(pathToTemplate)

        templateService.delete(FILE_NAME)

        assertFalse(Files.exists(pathToTemplate))
    }

    @Test(expected = FileNotFoundException::class)
    fun deleteWhenTemplateNotExistsShouldThrowException() {
        templateService.delete(FILE_NAME)
    }

    private fun createMultiPartFile(): MockMultipartFile =
            MockMultipartFile(FILE_NAME, FILE_NAME, MULTIPART_FORM_DATA.type, "content".toByteArray())

    companion object {
        private const val FILE_NAME = "template.ftl"
    }

}
