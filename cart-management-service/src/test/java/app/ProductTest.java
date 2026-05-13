package app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for Product domain object.
 * Validates constructor constraints and immutability.
 */
@DisplayName("Product domain validation")
class ProductTest {

    @Nested
    @DisplayName("Constructor validation")
    class ConstructorValidation {

        @Test
        @DisplayName("should throw when id is null")
        void shouldRejectNullId() {
            assertThatThrownBy(() -> new Product(null, "Valid Name", BigDecimal.TEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("ID cannot be null");
        }

        @Test
        @DisplayName("should throw when name is null")
        void shouldRejectNullName() {
            assertThatThrownBy(() -> new Product(1L, null, BigDecimal.TEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("name cannot be blank");
        }

        @Test
        @DisplayName("should throw when name is blank")
        void shouldRejectBlankName() {
            assertThatThrownBy(() -> new Product(1L, "   ", BigDecimal.TEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("name cannot be blank");
        }

        @Test
        @DisplayName("should throw when name is empty")
        void shouldRejectEmptyName() {
            assertThatThrownBy(() -> new Product(1L, "", BigDecimal.TEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("name cannot be blank");
        }

        @Test
        @DisplayName("should throw when price is null")
        void shouldRejectNullPrice() {
            assertThatThrownBy(() -> new Product(1L, "Valid Name", null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("price cannot be negative");
        }

        @Test
        @DisplayName("should throw when price is negative")
        void shouldRejectNegativePrice() {
            assertThatThrownBy(() -> new Product(1L, "Valid Name", new BigDecimal("-10.00")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("price cannot be negative");
        }

        @Test
        @DisplayName("should accept zero price")
        void shouldAcceptZeroPrice() {
            assertThatCode(() -> new Product(1L, "Free Item", BigDecimal.ZERO))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should accept valid product")
        void shouldCreateValidProduct() {
            Product product = new Product(42L, "Test Product", new BigDecimal("19.99"));

            assertThat(product.getId()).isEqualTo(42L);
            assertThat(product.getName()).isEqualTo("Test Product");
            assertThat(product.getPrice()).isEqualByComparingTo("19.99");
        }

        @Test
        @DisplayName("should trim name whitespace")
        void shouldTrimName() {
            Product product = new Product(1L, "  Padded Name  ", BigDecimal.TEN);
            assertThat(product.getName()).isEqualTo("Padded Name");
        }
    }

    @Nested
    @DisplayName("Object methods")
    class ObjectMethods {

        @Test
        @DisplayName("equals should compare by ID only")
        void equalsShouldCompareById() {
            Product p1 = new Product(1L, "First", BigDecimal.TEN);
            Product p2 = new Product(1L, "Different", BigDecimal.ONE);

            assertThat(p1).isEqualTo(p2);
        }

        @Test
        @DisplayName("hashCode should be consistent with equals")
        void hashCodeShouldBeConsistent() {
            Product p1 = new Product(1L, "First", BigDecimal.TEN);
            Product p2 = new Product(1L, "Different", BigDecimal.ONE);

            assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
        }

        @Test
        @DisplayName("toString should contain key fields")
        void toStringShouldFormatCorrectly() {
            Product product = new Product(5L, "Keyboard", new BigDecimal("89.50"));
            String result = product.toString();

            assertThat(result).contains("5", "Keyboard", "89.50");
        }
    }
}