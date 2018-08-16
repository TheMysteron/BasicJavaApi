package api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

	private static Logger logger = LoggerFactory.getLogger(JacksonConfig.class);

	@Bean
	public Jackson2ObjectMapperBuilder jacksonBuilder() {

		logger.debug("Custom Jackson2ObjectMapperBuilder specifed");

		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.serializationInclusion(JsonInclude.Include.NON_NULL)
				.indentOutput(true);
		return builder;
	}

}