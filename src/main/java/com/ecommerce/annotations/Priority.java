package com.ecommerce.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to assign priority levels to test methods.
 * Example usage: @Priority("High")
 */
@Retention(RetentionPolicy.RUNTIME) // The annotation will be available at runtime
@Target(ElementType.METHOD)       // This annotation can be applied to methods
public @interface Priority {
    String value(); // The priority level (e.g., "High", "Medium", "Low", "Critical")
}