package com.akson.invaders.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class TestConfiguration {

    @Autowired
    Environment environment;

    @Bean
    @Primary
    public TestRestTemplate testRestTemplate() {
        TestRestTemplate testRestTemplate = new TestRestTemplate(TestRestTemplate.HttpClientOption.ENABLE_REDIRECTS,
                TestRestTemplate.HttpClientOption.ENABLE_COOKIES);

        LocalHostUriTemplateHandler handler =
                new LocalHostUriTemplateHandler(environment, "http");
        testRestTemplate.setUriTemplateHandler(handler);

        return testRestTemplate;
    }

}
