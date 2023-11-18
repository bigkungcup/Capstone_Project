package sit.cp23ej2.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sit.cp23ej2.properties.FileStorageProperties;

@Configuration
@EnableConfigurationProperties({
                FileStorageProperties.class
})
public class ApplicationConfig {
        @Bean
        public ModelMapper modelMapper() {
                return new ModelMapper();
        }

}
