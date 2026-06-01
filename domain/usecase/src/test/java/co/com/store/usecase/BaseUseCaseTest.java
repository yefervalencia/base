package co.com.store.usecase;

import org.junit.jupiter.api.Test;

import co.com.store.usecase.BaseUseCase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseUseCaseTest {

    private final BaseUseCase useCase = new BaseUseCase() {
    };

    @Test
    void validateNotNullShouldThrowWhenObjectIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> useCase.validateNotNull(null, "Object cannot be null"));
    }

    @Test
    void validateNotNullShouldPassWhenObjectExists() {
        assertDoesNotThrow(() -> useCase.validateNotNull("value", "Error"));
    }

    @Test
    void validateNotEmptyShouldThrowWhenStringIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> useCase.validateNotEmpty(null, "String cannot be empty"));
    }

    @Test
    void validateNotEmptyShouldThrowWhenStringIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> useCase.validateNotEmpty("   ", "String cannot be empty"));
    }

    @Test
    void validateNotEmptyShouldPassWhenStringHasText() {
        assertDoesNotThrow(() -> useCase.validateNotEmpty("value", "Error"));
    }

    @Test
    void validateShouldThrowWhenConditionIsFalse() {
        assertThrows(IllegalArgumentException.class,
                () -> useCase.validate(false, "Condition failed"));
    }

    @Test
    void validateShouldPassWhenConditionIsTrue() {
        assertDoesNotThrow(() -> useCase.validate(true, "Error"));
    }
}
