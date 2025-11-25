package site.haechan.sns_backend.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import site.haechan.sns_backend.global.config.security.filter.JwtAuthenticationFilter;

@WebMvcTest
public abstract class ControllerTest {
	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;
	@MockitoBean
	protected JwtAuthenticationFilter jwtAuthenticationFilter;
}
