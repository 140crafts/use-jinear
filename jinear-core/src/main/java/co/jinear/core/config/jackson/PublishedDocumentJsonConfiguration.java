package co.jinear.core.config.jackson;

import co.jinear.core.model.response.mcp.McpToolManifestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class PublishedDocumentJsonConfiguration implements WebMvcConfigurer {

    private static final List<Class<?>> PUBLISHED_DOCUMENTS = List.of(McpToolManifestResponse.class);

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream()
                .filter(MappingJackson2HttpMessageConverter.class::isInstance)
                .map(MappingJackson2HttpMessageConverter.class::cast)
                .forEach(this::prettyPrintPublishedDocuments);
    }

    private void prettyPrintPublishedDocuments(MappingJackson2HttpMessageConverter converter) {
        ObjectMapper prettyPrinter = converter.getObjectMapper().copy().enable(SerializationFeature.INDENT_OUTPUT);
        PUBLISHED_DOCUMENTS.forEach(type -> converter.registerObjectMappersForType(type,
                registrations -> registrations.put(MediaType.APPLICATION_JSON, prettyPrinter)));
    }
}
