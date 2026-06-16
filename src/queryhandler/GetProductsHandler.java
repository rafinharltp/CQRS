package queryhandler;

import model.Product;
import query.GetProductsQuery;
import repository.ProductRepository;

import java.util.List;

public class GetProductsHandler {

    private final ProductRepository repository;

    public GetProductsHandler(
            ProductRepository repository) {

        this.repository = repository;
    }

    public List<Product> handle(
            GetProductsQuery query) {

        return repository.findAll();
    }
}