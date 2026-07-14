package com.example.api.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * The use-case-per-class convention: one @Service class per use case, named *UseCase,
 * living in its application feature package, exposing exactly one public method `handle`.
 */
@AnalyzeClasses(packages = "com.example", importOptions = ImportOption.DoNotIncludeTests.class)
class UseCaseConventionTests {

    @ArchTest
    static final ArchRule useCasesLiveInApplication = classes()
        .that().haveSimpleNameEndingWith("UseCase")
        .should().resideInAPackage("com.example.application..")
        .allowEmptyShould(true);

    @ArchTest
    static final ArchRule useCasesExposeExactlyOneHandleMethod = classes()
        .that().haveSimpleNameEndingWith("UseCase")
        .and(DescribedPredicate.describe("are not interfaces", c -> !c.isInterface()))
        .should(exposeExactlyOnePublicMethodNamedHandle())
        .allowEmptyShould(true);

    private static ArchCondition<JavaClass> exposeExactlyOnePublicMethodNamedHandle() {
        return new ArchCondition<>("expose exactly one public method, named handle") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                var publicMethods = javaClass.getMethods().stream()
                    .filter(m -> m.getModifiers().contains(JavaModifier.PUBLIC))
                    .toList();
                boolean ok = publicMethods.size() == 1
                    && publicMethods.getFirst().getName().equals("handle");
                events.add(new SimpleConditionEvent(javaClass, ok,
                    javaClass.getName() + " exposes " + publicMethods.size()
                        + " public method(s); a use case exposes exactly one, named handle"));
            }
        };
    }
}
