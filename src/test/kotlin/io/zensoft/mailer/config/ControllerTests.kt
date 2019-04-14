package io.zensoft.mailer.config

import io.zensoft.mailer.property.AuthProperties
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

@RunWith(SpringRunner::class)
abstract class ControllerTests {

    @Autowired
    protected lateinit var mvc: MockMvc

    @MockBean
    protected lateinit var authProperties: AuthProperties


    @Before
    fun setUp() {
        given(authProperties.token).willReturn("test")
    }

}