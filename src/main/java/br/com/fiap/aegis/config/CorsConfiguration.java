package br.com.fiap.aegis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // libera todas as rotas API
                .allowedOrigins("*") // em produção, trocar o "*" pelo domínio do mobile
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT") // métodos permitidos
                .allowedHeaders("*"); // libera todos os cabeçalhos, incluindo o Authorization com JWT
    }
}
