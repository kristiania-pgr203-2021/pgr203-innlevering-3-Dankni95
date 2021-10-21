package no.kristiania.person;

import no.kristiania.http.Product;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonDaoTest {
    private ProductDao dao = new ProductDao(TestData.createDataSource());
    
    @Test
    void shouldRetrieveSavedPerson() throws SQLException {
        Product person = examplePerson();
        dao.save(person);
        assertThat(dao.retrieve(person.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(person);
    }

    @Test
    void shouldListPeople() throws SQLException {
        Product person = examplePerson();
        dao.save(person);
        Product anotherPerson = examplePerson();
        dao.save(anotherPerson);
        
        assertThat(dao.listAll())
                .extracting(Product::getId)
                .contains(person.getId(), anotherPerson.getId());
    }

    private Product examplePerson() {
        Product person = new Product();
        person.setProductName(TestData.pickOne("Johannes", "Janine", "James", "Jane", "Jill", "Jacob"));
        person.setDescription(TestData.pickOne("Smith", "Williams", "Jones"));
        return person;
    }

}
