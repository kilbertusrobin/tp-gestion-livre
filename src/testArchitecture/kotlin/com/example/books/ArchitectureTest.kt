package com.example.books

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ArchitectureTest {

    private val importedClasses = ClassFileImporter().importPackages("com.example.books")

    @Test
    fun `le domaine ne doit pas dépendre de l'infrastructure`() {
        noClasses()
            .that().resideInAPackage("com.example.books.domain..")
            .should().dependOnClassesThat().resideInAPackage("com.example.books.infrastructure..")
            .check(importedClasses)
    }

    @Test
    fun `le use case ne doit pas dépendre du driving ni du driven`() {
        noClasses()
            .that().resideInAPackage("com.example.books.domain.usecase..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                "com.example.books.infrastructure.driving..",
                "com.example.books.infrastructure.driven..",
            )
            .check(importedClasses)
    }

    @Test
    fun `le driven ne doit pas dépendre du driving`() {
        noClasses()
            .that().resideInAPackage("com.example.books.infrastructure.driven..")
            .should().dependOnClassesThat().resideInAPackage("com.example.books.infrastructure.driving..")
            .check(importedClasses)
    }
}
