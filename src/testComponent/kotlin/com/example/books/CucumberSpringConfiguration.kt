package com.example.books

import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootApplication(
    scanBasePackages = [
        "com.example.books.domain",
        "com.example.books.infrastructure.driving",
        "com.example.books.infrastructure.application",
    ],
)
class ComponentTestApplication

@CucumberContextConfiguration
@SpringBootTest(
    classes = [ComponentTestApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.autoconfigure.exclude=" +
            "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
            "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
            "org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration",
    ],
)
@Import(InMemoryBookRepositoryTestConfig::class)
class CucumberSpringConfiguration
