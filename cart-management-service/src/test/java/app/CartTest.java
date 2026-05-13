package app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for Cart component.
 * Uses ApplicationContextRunner to avoid loading CommandLineRunner beans.
 */
@DisplayName("Cart component behavior")
class CartTest {

    // Lightweight context runner that loads ONLY Cart bean with prototype scope
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(CartConfig.class);

    /**
     * Minimal config to register Cart as prototype bean for testing.
     */
    static class CartConfig {
        @org.springframework.context.annotation.Bean
        @org.springframework.context.annotation.Scope("prototype")
        public Cart cart() {
            return new Cart();
        }
    }

    @Nested
    @DisplayName("Add product")
    class AddProductTests {

        @Test
        @DisplayName("should reject null product")
        void shouldRejectNullProduct() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                assertThatThrownBy(() -> cart.addProduct(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("cannot be null");
            });
        }

        @Test
        @DisplayName("should add valid product")
        void shouldAddValidProduct() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                Product product = new Product(1L, "Test Item", BigDecimal.TEN);

                boolean result = cart.addProduct(product);

                assertThat(result).isTrue();
                assertThat(cart.getItems()).hasSize(1);
                assertThat(cart.getItems().get(0)).isSameAs(product);
            });
        }

        @Test
        @DisplayName("should allow duplicate products in cart")
        void shouldAllowDuplicateProducts() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                Product product = new Product(1L, "Test Item", BigDecimal.TEN);

                cart.addProduct(product);
                cart.addProduct(product);

                assertThat(cart.getItems()).hasSize(2);
            });
        }
    }

    @Nested
    @DisplayName("Remove product")
    class RemoveProductTests {

        @Test
        @DisplayName("should return false when removing from empty cart")
        void shouldReturnFalseForEmptyCart() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                boolean result = cart.removeProduct(1L);
                assertThat(result).isFalse();
            });
        }

        @Test
        @DisplayName("should remove single matching product")
        void shouldRemoveSingleMatchingProduct() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                Product p1 = new Product(1L, "Item A", new BigDecimal("10.00"));
                Product p2 = new Product(1L, "Item A", new BigDecimal("10.00"));

                cart.addProduct(p1);
                cart.addProduct(p2);

                boolean result = cart.removeProduct(1L);

                assertThat(result).isTrue();
                assertThat(cart.getItems()).hasSize(1); // Only ONE removed
            });
        }

        @Test
        @DisplayName("should remove correct product by ID")
        void shouldRemoveCorrectProductById() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                Product p1 = new Product(1L, "First", BigDecimal.TEN);
                Product p2 = new Product(2L, "Second", BigDecimal.ONE);

                cart.addProduct(p1);
                cart.addProduct(p2);
                cart.removeProduct(1L);

                assertThat(cart.getItems()).extracting(Product::getId)
                        .containsExactly(2L);
            });
        }

        @Test
        @DisplayName("should handle null ID gracefully")
        void shouldHandleNullId() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                Product product = new Product(1L, "Test", BigDecimal.TEN);
                cart.addProduct(product);

                boolean result = cart.removeProduct(null);

                assertThat(result).isFalse();
                assertThat(cart.getItems()).hasSize(1);
            });
        }
    }

    @Nested
    @DisplayName("Cart calculations")
    class CalculationTests {

        @Test
        @DisplayName("should calculate total price correctly")
        void shouldCalculateTotalPrice() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                cart.addProduct(new Product(1L, "A", new BigDecimal("10.50")));
                cart.addProduct(new Product(2L, "B", new BigDecimal("5.25")));
                cart.addProduct(new Product(3L, "C", new BigDecimal("4.25")));

                BigDecimal total = cart.getTotalPrice();
                assertThat(total).isEqualByComparingTo("20.00");
            });
        }

        @Test
        @DisplayName("should return zero for empty cart")
        void shouldReturnZeroForEmptyCart() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                assertThat(cart.getTotalPrice()).isEqualByComparingTo("0.00");
            });
        }

        @Test
        @DisplayName("getItems should return defensive copy")
        void getItemsShouldReturnDefensiveCopy() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                Product product = new Product(1L, "Test", BigDecimal.TEN);
                cart.addProduct(product);

                List<Product> items = cart.getItems();
                items.clear();

                assertThat(cart.getItems()).hasSize(1);
            });
        }

        @Test
        @DisplayName("clear should empty the cart")
        void clearShouldEmptyCart() {
            contextRunner.run(context -> {
                Cart cart = context.getBean(Cart.class);
                cart.addProduct(new Product(1L, "A", BigDecimal.TEN));
                cart.addProduct(new Product(2L, "B", BigDecimal.ONE));
                cart.clear();

                assertThat(cart.getItems()).isEmpty();
                assertThat(cart.getTotalPrice()).isEqualByComparingTo("0.00");
            });
        }
    }

    @Nested
    @DisplayName("Prototype scope isolation")
    class PrototypeIsolationTests {

        @Test
        @DisplayName("each bean request should return new cart instance")
        void eachBeanRequestShouldReturnNewInstance() {
            contextRunner.run(context -> {
                Cart cart1 = context.getBean(Cart.class);
                Cart cart2 = context.getBean(Cart.class);

                assertThat(cart1).isNotSameAs(cart2);
            });
        }

        @Test
        @DisplayName("modifying one cart should not affect another")
        void modifyingOneCartShouldNotAffectAnother() {
            contextRunner.run(context -> {
                Cart cart1 = context.getBean(Cart.class);
                Cart cart2 = context.getBean(Cart.class);

                cart1.addProduct(new Product(1L, "Item", BigDecimal.TEN));

                assertThat(cart1.getItems()).hasSize(1);
                assertThat(cart2.getItems()).isEmpty();
            });
        }

        @Test
        @DisplayName("total price should be isolated between carts")
        void totalPriceShouldBeIsolated() {
            contextRunner.run(context -> {
                Cart cart1 = context.getBean(Cart.class);
                Cart cart2 = context.getBean(Cart.class);

                cart1.addProduct(new Product(1L, "A", new BigDecimal("100.00")));
                cart2.addProduct(new Product(2L, "B", new BigDecimal("50.00")));

                assertThat(cart1.getTotalPrice()).isEqualByComparingTo("100.00");
                assertThat(cart2.getTotalPrice()).isEqualByComparingTo("50.00");
            });
        }
    }
}