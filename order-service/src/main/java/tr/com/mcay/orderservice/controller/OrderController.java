package tr.com.mcay.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tr.com.mcay.orderservice.dto.StockRequest;
import tr.com.mcay.orderservice.model.Orders;
import tr.com.mcay.orderservice.service.OrderService;


import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Controller", description = "API to manage orders")
public class OrderController {


    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Get all orders", description = "Retrieves a list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of orders",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class)))
    })
    @GetMapping
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Get order by ID", description = "Retrieves an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the order",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public Orders getOrderById(@Parameter(description = "ID of the order to retrieve") @PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @Operation(summary = "Create a new order", description = "Creates a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the order",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class)))
    })
    @PostMapping
    public Orders createOrder(@Parameter(description = "Order to create") @RequestBody Orders order) {
        return orderService.createOrder(order);
    }

    @Operation(summary = "Update an order", description = "Updates an existing order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the order",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{id}")
    public Orders updateOrder(
            @Parameter(description = "ID of the order to update") @PathVariable Long id,
            @Parameter(description = "Updated order information") @RequestBody Orders order) {
        order.setId(id);
        return orderService.updateOrder(order);
    }

    @Operation(summary = "Delete an order", description = "Deletes an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    public void deleteOrder(@Parameter(description = "ID of the order to delete") @PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @Operation(summary = "Get product details", description = "Retrieves product details by product ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the product details"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/product/{id}")
    public String getProductDetails(@Parameter(description = "ID of the product to retrieve details for") @PathVariable Long id) {
        return orderService.getProductDetails(id);
    }

    @Operation(summary = "Get product details with retry", description = "Retrieves product details by product ID with retry mechanism")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the product details"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/product/retry/{id}")
    public String getProductDetailsRetry(@Parameter(description = "ID of the product to retrieve details for") @PathVariable Long id) {
        return orderService.getProductDetailsRetry(id);
    }

    @Operation(summary = "Create stock", description = "Creates stock for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the stock"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/create/stock")
    public String createStock(@Parameter(description = "Stock request with product ID and quantity") @RequestBody StockRequest stockRequest) {
        return orderService.createStock(stockRequest.getProductId(), stockRequest.getQuantity());
    }
}
