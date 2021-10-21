package no.kristiania.person;

import no.kristiania.http.Product;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonnelServerTest {


    public PersonnelServerTest() throws IOException {
    }

    @Test
    void shouldListRolesFromDatabase() throws IOException, SQLException {
        CategoryDao roleDao = new CategoryDao(TestData.createDataSource());

    }

    @Test
    void shouldCreateNewPerson() throws IOException, SQLException {
        ProductDao personDao = new ProductDao(TestData.createDataSource());
        
        assertThat(personDao.listAll())
                .extracting(Product::getDescription)
                .contains("Persson");
    }

    @Test
    void shouldListPeople() throws SQLException, IOException {
        ProductDao personDao = new ProductDao(TestData.createDataSource());
        
        Product person = new Product();
        person.setProductName("Noen Andre");
        person.setDescription("Persson");
        personDao.save(person);
    }
}
