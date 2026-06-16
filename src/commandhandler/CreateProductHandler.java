package commandhandler;

import command.CreateProductCommand;
import model.Product;
import repository.ProductRepository;

public class CreateProductHandler {

    private final ProductRepository repository;

    public CreateProductHandler(
            ProductRepository repository) {

        this.repository = repository;
    }

    public void handle(
            CreateProductCommand command) {

        Product product =
                new Product(
                        command.getName(),
                        command.getPrice()
                );

        repository.save(product);
    }
}