package com.ecommerce.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to categorize test methods by application feature.
 * Example usage: @Feature("Authentication")
 */
@Retention(RetentionPolicy.RUNTIME) // The annotation will be available at runtime
@Target(ElementType.METHOD)       // This annotation can be applied to methods
public @interface Feature {
    String value(); // The name of the feature (e.g., "Login", "Checkout", "Product Management")
}