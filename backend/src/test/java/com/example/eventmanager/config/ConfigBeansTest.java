package com.example.eventmanager.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.assertj.core.api.Assertions.assertThat;

class ConfigBeansTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(
                    SecurityConfig.class,
                    WebConfig.class,
                    SwaggerConfig.class
            );

    @Test
    void securityConfig_shouldProvidePasswordEncoderBean() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(PasswordEncoder.class);
            assertThat(context.getBean(PasswordEncoder.class).encode("test"))
                    .isNotBlank();
        });
    }

    @Test
    void securityConfig_shouldProvideSecurityFilterChainBean() {
        contextRunner.run(context -> {
            assertThat(context).hasBean("filterChain");
        });
    }

    @Test
    void webConfig_shouldProvideCorsConfigurer() {
        contextRunner.run(context -> {
            assertThat(context).hasBean("corsConfigurer");
        });
    }

    @Test
    void swaggerConfig_shouldLoadIntoContext() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(SwaggerConfig.class);
        });
    }
}
