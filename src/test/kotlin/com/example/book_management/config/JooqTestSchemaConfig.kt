// src/test/kotlin/.../JooqTestSchemaConfig.kt
package example.testconfig

import org.jooq.conf.MappedSchema
import org.jooq.conf.RenderMapping
import org.jooq.impl.DefaultConfiguration
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@TestConfiguration
@Profile("test")
class JooqTestSchemaConfig {

    @Bean
    fun jooqSchemaMappingCustomizer() = DefaultConfigurationCustomizer { cfg: DefaultConfiguration ->
        val mapping = RenderMapping().withSchemata(
            MappedSchema().withInput("dev").withOutput("test")
        )
        val merged = cfg.settings()
            .withRenderMapping(mapping)
        cfg.setSettings(merged)
    }
}
