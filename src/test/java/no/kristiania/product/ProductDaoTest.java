package no.kristiania.product;

import no.kristiania.http.Category;
import no.kristiania.http.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDaoTest {
    private static CategoryDao categoryDao = null;
    private final ProductDao dao = new ProductDao(TestData.createDataSource());

    @BeforeAll
    static void initCategory() throws SQLException {
        categoryDao = new CategoryDao(TestData.createDataSource());
        Category category = new Category();
        category.setCategoryName("electric");
        Category categoryTwo = new Category();
        categoryTwo.setCategoryName("diesel");
        Category categoryThree = new Category();
        categoryThree.setCategoryName("petrol");

        categoryDao.save(category);
        categoryDao.save(categoryTwo);
        categoryDao.save(categoryThree);
    }


    @Test
    void shouldRetrieveSavedProduct() throws SQLException {
        Product product = exampleProduct();
        product.setCategoryId(categoryDao.retrieveCategoryIdByName("electric"));
        dao.save(product);
        assertThat(dao.retrieve(product.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    void shouldListProducts() throws SQLException {
        Product product = exampleProduct();
        product.setCategoryId(categoryDao.retrieveCategoryIdByName("electric"));
        dao.save(product);

        Product anotherProduct = exampleProduct();
        anotherProduct.setCategoryId(categoryDao.retrieveCategoryIdByName("diesel"));
        dao.save(anotherProduct);


        assertThat(dao.listAll())
                .extracting(Product::getId)
                .contains(product.getId(), anotherProduct.getId());
    }


    @Test
    void shouldListProductsByCategory() throws SQLException {
        Product matchingProduct = exampleProduct();
        matchingProduct.setCategoryId(categoryDao.retrieveCategoryIdByName("electric"));
        dao.save(matchingProduct);

        Product anotherMatchingProduct = exampleProduct();
        anotherMatchingProduct.setCategoryId(categoryDao.retrieveCategoryIdByName("electric"));
        dao.save(anotherMatchingProduct);

        Product nonMatchingProduct = exampleProduct();
        nonMatchingProduct.setCategoryId(categoryDao.retrieveCategoryIdByName("petrol"));
        dao.save(nonMatchingProduct);


        assertThat(dao.listProductsByCategory(matchingProduct.getCategoryId()))
                .extracting(Product::getId)
                .contains(matchingProduct.getCategoryId(), anotherMatchingProduct.getCategoryId())
                .doesNotContain(nonMatchingProduct.getCategoryId());
    }

    private Product exampleProduct() {
        Product product = new Product();
        product.setProductName(TestData.pickOne("BMW", "AUDI", "Tesla", "Toyota", "Prius", "Lambo"));
        product.setDescription(TestData.pickOne("Very good car", "best car in the market", "buy 1 get 1 for free"));
        product.setPrice(Integer.parseInt(TestData.pickOne("100000", "400000", "3000000", "4050000", "5012990")));
        return product;
    }

}
