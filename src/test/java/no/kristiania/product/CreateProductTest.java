package no.kristiania.product;

import no.kristiania.http.Category;
import no.kristiania.http.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateProductTest {
 private static CategoryDao categoryDao;


    @BeforeAll
    static void initCategory() throws SQLException {
        categoryDao = new CategoryDao(TestData.createDataSource());
        Category category = new Category();
        category.setCategoryName("Random category");
        Category categoryTwo = new Category();
        categoryTwo.setCategoryName("Another one");
        categoryDao.save(category);
        categoryDao.save(categoryTwo);
    }


    @Test
    void shouldListProduct() throws SQLException {
        ProductDao productDao = new ProductDao(TestData.createDataSource());
        
        assertThat(productDao.listAll())
                .extracting(Product::getDescription)
                .contains("En bil, ja");
    }

    @Test
    void shouldCreateNewProduct() throws SQLException {
        ProductDao productDao = new ProductDao(TestData.createDataSource());
        
        Product product = new Product();
        product.setProductName("BMW");
        product.setDescription("En bil, ja");
        product.setPrice(100);
        product.setCategoryId(categoryDao.retrieveCategoryIdByName(TestData.pickOne("Random category","Another one")));
        productDao.save(product);
    }
}
