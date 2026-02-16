package com.example.pragmatic.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

class ApiConventionTest {

    private static final JavaClasses CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.example.pragmatic");

    @Test
    void RestController는_api_경로로_시작해야_한다() {
        classes()
                .that().areAnnotatedWith(RestController.class)
                .and().areAnnotatedWith(RequestMapping.class)
                .should(haveRequestMappingStartingWithApi())
                .allowEmptyShould(true)
                .check(CLASSES);
    }

    @Test
    void 필드_주입을_사용하지_않아야_한다() {
        NO_CLASSES_SHOULD_USE_FIELD_INJECTION
                .allowEmptyShould(true)
                .check(CLASSES);
    }

    private static ArchCondition<JavaClass> haveRequestMappingStartingWithApi() {
        return new ArchCondition<>("have @RequestMapping value starting with /api") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                Optional<String[]> paths = javaClass.tryGetAnnotationOfType(RequestMapping.class)
                        .map(RequestMapping::value);

                if (paths.isEmpty() || paths.get().length == 0) {
                    events.add(SimpleConditionEvent.violated(
                            javaClass,
                            String.format("Controller %s must declare @RequestMapping path", javaClass.getSimpleName())
                    ));
                    return;
                }

                String path = paths.get()[0];
                if (!path.startsWith("/api")) {
                    events.add(SimpleConditionEvent.violated(
                            javaClass,
                            String.format("Controller %s has invalid base path '%s'", javaClass.getSimpleName(), path)
                    ));
                }
            }
        };
    }

}
