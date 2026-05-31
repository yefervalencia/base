package co.com.base.appservice;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class ArchitectureRulesTest {

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages("co.com.base");

    @Test
    void mongoShouldNotDependOnUseCaseLayer() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("co.com.base.mongo..")
                .should().dependOnClassesThat().resideInAPackage("co.com.base.usecase..")
                .check(importedClasses);
    }

    @Test
    void apiShouldNotDependDirectlyOnMongoLayer() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("co.com.base.api..")
                .should().dependOnClassesThat().resideInAPackage("co.com.base.mongo..")
                .check(importedClasses);
    }
}
