package no.kristiania.product;

import no.kristiania.http.Category;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryDaoTest {

    private final CategoryDao dao = new CategoryDao(TestData.createDataSource());

    @Test
    void shouldListSavedRoles() throws SQLException {
        String categoryName = "Category-" + UUID.randomUUID();
        Category category = new Category();
        category.setCategoryName(categoryName);
        dao.save(category);


        String categoryNameTwo = "Category-" + UUID.randomUUID();
        Category categoryTwo = new Category();
        categoryTwo.setCategoryName(categoryNameTwo);
        dao.save(categoryTwo);


        assertThat(dao.listAll())
                .extracting(Category::getCategoryName)
                .contains(category.getCategoryName(), categoryTwo.getCategoryName());
    }

    @Test
    void shouldRetrieveSavedCategory() throws SQLException {
        Category category = exampleCategory();

        assertThat(dao.retrieve(category.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(category);
    }

    @Test
    void shouldRetrieveCategoryIdByName() throws SQLException {
        Category category = new Category();
        category.setCategoryName("BMW");
        dao.save(category);

       Long categoryId = dao.retrieveCategoryIdByName(category.getCategoryName());

        assertThat(dao.retrieve(categoryId))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(category);
    }

    private Category exampleCategory() throws SQLException {
        Category category = new Category();
        category.setCategoryName("Tesla");
        dao.save(category);
        return category;
    }

}
