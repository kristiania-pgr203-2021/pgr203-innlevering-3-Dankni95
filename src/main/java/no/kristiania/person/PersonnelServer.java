package no.kristiania.person;

import no.kristiania.http.Category;
import no.kristiania.http.Product;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PersonnelServer {
    private static final Logger logger = LoggerFactory.getLogger(PersonnelServer.class);

    public static void main(String[] args) throws IOException, SQLException {


        logger.info("Started server");
        DataSource dataSource = createDataSource();
        CategoryDao categoryDao = new CategoryDao(dataSource);
        categoryDao.save("Kategori 1");

        // server.addController("/api/roleOptions", new RoleOptionsController(new CategoryDao(dataSource)));
        // server.addController("/api/newPerson", new CreatePersonController(new ProductDao(dataSource)));
        // server.addController("/api/people", new ListPeopleController(new ProductDao(dataSource)));
        Scanner scanner = new Scanner(System.in);


        showAlternatives();
        String input = scanner.nextLine();
        while (!input.equals("!q")) {

            switch (input) {
                case "01":
                    String result;
                    System.out.println("Enter product name");
                    ProductDao productDao = new ProductDao(dataSource);
                    Product product = new Product();
                    result = scanner.nextLine();
                    product.setProductName(result);
                    System.out.println("Add description");
                    result = scanner.nextLine();
                    product.setDescription(result);
                    System.out.println("Enter product price");
                    result = scanner.nextLine();
                    product.setPrice(Integer.parseInt(result));

                    chooseCategory(productDao, product, dataSource, result,scanner);
                    showAlternatives(input, scanner);
                    input = scanner.nextLine();
                    break;
                case "02":
                    showAlternatives(input, scanner);
                    break;
                case "03":
                    showAlternatives(input, scanner);
                    break;
                case "04":
                    showAlternatives(input, scanner);
                    break;
                case "05":
                    showAlternatives(input, scanner);
                    break;
                default:
                    showAlternatives(input, scanner);
                    break;

            }


        }


    }

    private static void chooseCategory(ProductDao productDao, Product product, DataSource dataSource,String input, Scanner sc) throws SQLException {
        CategoryDao categoryDao = new CategoryDao(dataSource);
        System.out.println("Choose category from list below");
        List<String> category = categoryDao.listAll();
        for (String c : category ) System.out.println(c);
        input = sc.nextLine();

        System.out.println(categoryDao.retrieveName(input));

    }

    private static void showAlternatives(String input, Scanner scanner) {
        System.out.println("01 Add product");
        System.out.println("02 Add categories");
        System.out.println("03 List categories");
        System.out.println("04 List all products");
        System.out.println("05 List all products by category");
        System.out.println("Write !q to quit");
        input = scanner.nextLine();
    }
    private static void showAlternatives() {
        System.out.println("01 Add product");
        System.out.println("02 Add categories");
        System.out.println("03 List categories");
        System.out.println("04 List all products");
        System.out.println("05 List all products by category");
        System.out.println("Write !q to quit");
    }

    private static DataSource createDataSource() throws IOException {


        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(("jdbc:postgresql://localhost:5432/person_db"));
        dataSource.setUser("person_dbuser");
        dataSource.setPassword("testingPasswordLOL");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
