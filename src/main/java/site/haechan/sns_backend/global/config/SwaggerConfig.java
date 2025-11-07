package site.haechan.sns_backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;


//todo: 배포환경에서는 ip등 클라이언트의 환경에 따라 접근 제어설정 필요(내부용)
@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		Server server = new Server().url("http://localhost:8080").description("Local Server");
		return new OpenAPI()
			.addServersItem(server)
			.info(
				new Info()
					.title("SNS Backend API Documentation")
					.version("1.0.0")
					.description("SNS 서비스의 API 명세서입니다.")
			);
	}
}
