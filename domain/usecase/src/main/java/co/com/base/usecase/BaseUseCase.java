package co.com.base.usecase;

/**
 * Clase base para todos los casos de uso del proyecto.
 * Proporciona funcionalidades comunes reutilizables.
 * 
 * Para usar logging, inyecta un logger en tus implementaciones:
 * private static final Logger logger = LoggerFactory.getLogger(MiUseCase.class);
 */
public abstract class BaseUseCase {
    
    /**
     * Constructor protegido para evitar instanciación directa
     */
    protected BaseUseCase() {
        // Constructor base para logging o inicializaciones comunes
    }
    
    /**
     * Método para validar que un objeto no sea nulo
     * @param object Objeto a validar
     * @param message Mensaje de error si es nulo
     * @throws IllegalArgumentException Si el objeto es nulo
     */
    protected void validateNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Método para validar que una cadena no esté vacía
     * @param string Cadena a validar
     * @param message Mensaje de error si está vacía
     * @throws IllegalArgumentException Si la cadena está vacía
     */
    protected void validateNotEmpty(String string, String message) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Método para validar una condición
     * @param condition Condición a validar (debe ser true)
     * @param message Mensaje de error si la condición es false
     * @throws IllegalArgumentException Si la condición es false
     */
    protected void validate(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
