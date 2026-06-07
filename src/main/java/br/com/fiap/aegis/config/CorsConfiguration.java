package br.com.fiap.aegis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todas as rotas da API
                // 🌐 Troca de allowedOrigins para allowedOriginPatterns para aceitar credenciais
                .allowedOriginPatterns("*")
                // ⚡ Métodos permitidos organizados
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                // 📥 Libera todos os cabeçalhos (inclusive Authorization do JWT)
                .allowedHeaders("*")
                // 🔑 Permite envio de cookies e credenciais com segurança
                .allowCredentials(true);
    }
}