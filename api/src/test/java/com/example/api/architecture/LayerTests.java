package com.example.api.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * The dependency rule, test-time edition. Maven module boundaries already stop most
 * violations at compile time; these rules document the architecture and catch what
 * modules cannot express. If a rule fails, move the type to the right layer — don't
 * loosen the rule.
 */
@AnalyzeClasses(packages = "com.example", importOptions = ImportOption.DoNotIncludeTests.class)
class LayerTests {

    @ArchTest
    static final ArchRule dependencyRule = layeredArchitecture()
        .consideringOnlyDependenciesInLayers()
        .withOptionalLayers(true)
        .layer("SharedKernel").definedBy("com.example.sharedkernel..")
        .layer("Domain").definedBy("com.example.domain..")
        .layer("Application").definedBy("com.example.application..")
        .layer("Infrastructure").definedBy("com.example.infrastructure..")
        .layer("Api").definedBy("com.example.api..")
        .whereLayer("Api").mayNotBeAccessedByAnyLayer()
        .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Api")
        .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure", "Api")
        .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure", "Api")
        .whereLayer("SharedKernel")
        .mayOnlyBeAccessedByLayers("Domain", "Application", "Infrastructure", "Api");

    @ArchTest
    static final ArchRule sharedKernelIsJdkOnly = classes()
        .that().resideInAPackage("com.example.sharedkernel..")
        .should().onlyDependOnClassesThat()
        .resideInAnyPackage("com.example.sharedkernel..", "java..")
        .allowEmptyShould(true);

    @ArchTest
    static final ArchRule domainIsFrameworkFree = noClasses()
        .that().resideInAPackage("com.example.domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "org.springframework..", "org.hibernate..", "com.fasterxml..", "jakarta.validation..")
        .because("domain may use jakarta.persistence mapping annotations only "
            + "(the documented trade-off) — never Spring, Hibernate or Jackson")
        .allowEmptyShould(true);

    @ArchTest
    static final ArchRule applicationStaysOutOfTheWeb = noClasses()
        .that().resideInAPackage("com.example.application..")
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "org.springframework.web..", "org.springframework.data..", "jakarta.persistence..",
            "jakarta.servlet..")
        .because("application holds use cases and ports — HTTP and persistence live outside")
        .allowEmptyShould(true);
}
