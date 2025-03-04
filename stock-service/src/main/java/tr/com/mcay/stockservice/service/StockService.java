package tr.com.mcay.stockservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.mcay.stockservice.exception.ResourceNotFoundException;
import tr.com.mcay.stockservice.exception.BadRequestException;
import tr.com.mcay.stockservice.model.Stock;
import tr.com.mcay.stockservice.repository.StockRepository;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;
    
    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
    
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
    
    public Stock getStockById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "id", id));
    }
    
    public Stock createStock(Stock stock) {
        if (stock.getProductId() == null) {
            throw new BadRequestException("Product ID cannot be null");
        }
        if (stock.getQuantity() <= 0) {
            throw new BadRequestException("Stock quantity must be greater than zero");
        }
        return stockRepository.save(stock);
    }
    
    public Stock updateStock(Stock stock) {
        if (stock.getId() == null) {
            throw new BadRequestException("Stock ID cannot be null for update operation");
        }
        // Check if stock exists
        stockRepository.findById(stock.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "id", stock.getId()));
        
        if (stock.getProductId() == null) {
            throw new BadRequestException("Product ID cannot be null");
        }
        if (stock.getQuantity() < 0) {
            throw new BadRequestException("Stock quantity cannot be negative");
        }
        
        return stockRepository.save(stock);
    }
    
    public void deleteStock(Long id) {
        // Check if stock exists before deleting
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "id", id));
        stockRepository.delete(stock);
    }
}
