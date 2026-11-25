package co.jinear.core.config.jackson;

import co.jinear.core.model.response.mcp.McpToolManifestResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Configuration
public class PublishedDocumentJsonConfiguration implements WebMvcConfigurer {

    private static final List<Class<?>> PUBLISHED_DOCUMENTS = List.of(McpToolManifestResponse.class);

    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        builder.configureMessageConverters(this::prettyPrintPublishedDocuments);
    }

    private void prettyPrintPublishedDocuments(HttpMessageConverter<?> converter) {
        if (converter instanceof JacksonJsonHttpMessageConverter jsonConverter) {
            JsonMapper prettyPrinter = jsonConverter.getMapper().rebuild().enable(SerializationFeature.INDENT_OUTPUT).build();
            PUBLISHED_DOCUMENTS.forEach(type -> jsonConverter.registerMappersForType(type,
                    registrations -> registrations.put(MediaType.APPLICATION_JSON, prettyPrinter)));
        }
    }
}
