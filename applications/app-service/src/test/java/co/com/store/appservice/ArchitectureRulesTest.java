package co.com.store.appservice;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class ArchitectureRulesTest {

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages("co.com.store");

    @Test
    void mongoShouldNotDependOnUseCaseLayer() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("co.com.store.mongo..")
                .should().dependOnClassesThat().resideInAPackage("co.com.store.usecase..")
                .check(importedClasses);
    }

    @Test
    void apiShouldNotDependDirectlyOnMongoLayer() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("co.com.store.api..")
                .should().dependOnClassesThat().resideInAPackage("co.com.store.mongo..")
                .check(importedClasses);
    }
}
