package tr.com.mcay.stockservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tr.com.mcay.stockservice.model.Stock;
import tr.com.mcay.stockservice.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@Tag(name = "Stock Controller", description = "API to manage stock")
public class StockController {
    private final StockService stockService;
    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Operation(summary = "Get all stocks", description = "Retrieves a list of all stocks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of stocks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class)))
    })
    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @Operation(summary = "Get stock by ID", description = "Retrieves a stock by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the stock",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    @GetMapping("/{id}")
    public Stock getStockById(@Parameter(description = "ID of the stock to retrieve") @PathVariable Long id) {
        return stockService.getStockById(id);
    }

    @Operation(summary = "Create a new stock", description = "Creates a new stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the stock",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class)))
    })
    @PostMapping
    public Stock createStock(@Parameter(description = "Stock to create") @RequestBody Stock stock) {
        return stockService.createStock(stock);
    }

    @Operation(summary = "Update a stock", description = "Updates an existing stock by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the stock",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Stock.class))),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    @PutMapping("/{id}")
    public Stock updateStock(
            @Parameter(description = "ID of the stock to update") @PathVariable Long id,
            @Parameter(description = "Updated stock information") @RequestBody Stock stock) {
        stock.setId(id);
        return stockService.updateStock(stock);
    }

    @Operation(summary = "Delete a stock", description = "Deletes a stock by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the stock"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    @DeleteMapping("/{id}")
    public void deleteStock(@Parameter(description = "ID of the stock to delete") @PathVariable Long id) {
        stockService.deleteStock(id);
    }
}
