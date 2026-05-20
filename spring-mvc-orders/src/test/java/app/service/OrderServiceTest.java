package app.service;

import app.exception.OrderValidationException;
import app.model.dto.CreateOrderRequest;
import app.model.dto.OrderResponse;
import app.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    private OrderService service;

    @BeforeEach
    void setUp() {
        service = new OrderService(repository);
    }

    @Test
    @DisplayName("createOrder returns response with calculated totalCost")
    void createOrder_returnsResponse() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setProducts(List.of(
                new app.model.Product("Keyboard", BigDecimal.valueOf(79.99)),
                new app.model.Product("Mouse", BigDecimal.valueOf(29.99))
        ));

        // Mock repository to return order with ID
        given(repository.save(any(app.model.Order.class))).willAnswer(invocation -> {
            var order = invocation.getArgument(0, app.model.Order.class);
            order.setId(1L);
            return order;
        });

        OrderResponse response = service.createOrder(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTotalCost()).isEqualTo(BigDecimal.valueOf(109.98));
        assertThat(response.getProducts()).hasSize(2);
    }

    @Test
    @DisplayName("createOrder throws when products list is empty")
    void createOrder_throws_whenEmptyProducts() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setProducts(List.of());

        assertThatThrownBy(() -> service.createOrder(request))
                .isInstanceOf(OrderValidationException.class);
    }

    @Test
    @DisplayName("getOrderById returns response when found")
    void getOrderById_returnsResponse() {
        var order = new app.model.Order(List.of(new app.model.Product("Test", BigDecimal.ONE)));
        order.setId(42L);
        given(repository.findById(42L)).willReturn(Optional.of(order));

        Optional<OrderResponse> result = service.getOrderById(42L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(42L);
    }

    @Test
    @DisplayName("getOrderById returns empty when not found")
    void getOrderById_returnsEmpty() {
        given(repository.findById(999L)).willReturn(Optional.empty());
        assertThat(service.getOrderById(999L)).isEmpty();
    }
}