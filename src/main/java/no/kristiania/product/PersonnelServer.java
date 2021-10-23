package no.kristiania.product;

import no.kristiania.http.Category;
import no.kristiania.http.Product;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public class PersonnelServer {
    private static final Logger logger = LoggerFactory.getLogger(PersonnelServer.class);

    public static void main(String[] args) throws IOException, SQLException {


        logger.info("Started server");
        DataSource dataSource = createDataSource();
        CategoryDao categoryDao = new CategoryDao(dataSource);
        ProductDao productDao = new ProductDao(dataSource);
        Product product = new Product();
        Category category = new Category();

        Scanner scanner = new Scanner(System.in);


        showAlternatives();
        String input = scanner.nextLine();
        while (!input.equals("!q")) {

            switch (input) {
                case "01":
                    String result;
                    System.out.println("Enter product name");
                    result = scanner.nextLine();
                    product.setProductName(result);
                    System.out.println("Add description");
                    result = scanner.nextLine();
                    product.setDescription(result);
                    System.out.println("Enter product price");
                    result = scanner.nextLine();
                    product.setPrice(Integer.parseInt(result));

                    chooseCategory(productDao, product, categoryDao, result, scanner);
                    input = showAlternatives(scanner);
                    break;
                case "02":
                    System.out.println("Enter category name");
                    result = scanner.nextLine();
                    category.setCategoryName(result);
                    categoryDao.save(category);
                    System.out.println("Saved category " + result);
                    input = showAlternatives(scanner);
                    break;
                case "03":
                    List<Category> categories = categoryDao.listAll();
                    for (Category c : categories) System.out.println(c.getCategoryName());
                    System.out.println("--------------------");
                    System.out.println();
                    input = showAlternatives(scanner);
                    break;
                case "04":
                    List<Product> productList = productDao.listAll();
                    for (Product p : productList) {
                        System.out.println(p.getProductName());
                    }
                    System.out.println("--------------------");
                    System.out.println(" ");
                    input = showAlternatives(scanner);
                    break;
                case "05":
                    String userInput;
                    System.out.println("Velg kategori: ");
                    List<Category> categoryList = categoryDao.listAll();
                    for (Category c : categoryList) System.out.println(c.getCategoryName());
                    System.out.println("--------------------");
                    userInput = scanner.nextLine();
                    List<Product> matchingProducts = productDao.listProductsByCategory(categoryDao.retrieveCategoryIdByName(userInput));
                    for (Product p : matchingProducts) System.out.println(p.getProductName());
                    System.out.println("--------------------");
                    input = showAlternatives(scanner);
                    break;
                default:
                    input = showAlternatives(scanner);
                    break;

            }


        }


    }

    private static void chooseCategory(ProductDao productDao, Product product, CategoryDao categoryDao, String input, Scanner sc) throws SQLException {

        if (categoryDao.listAll().isEmpty()) {
            addCategory(product, sc, categoryDao, productDao);
        } else {
            System.out.println("Choose category from list below");
            List<Category> category = categoryDao.listAll();
            for (Category c : category) System.out.println(c.getCategoryName());
            input = sc.nextLine();
            product.setCategoryId(categoryDao.retrieveCategoryIdByName(input));
            productDao.save(product);
            logger.info("Saved product: " + product.getProductName());
            System.out.println("--------------------");
            System.out.println(" ");
        }

    }

    private static void addCategory(Product product, Scanner scanner, CategoryDao categoryDao, ProductDao productDao) throws SQLException {
        String result;

        System.out.println("Enter category name");
        result = scanner.nextLine();
        Category category = new Category();
        category.setCategoryName(result);
        categoryDao.save(category);
        product.setCategoryId(categoryDao.retrieveCategoryIdByName(result));
        productDao.save(product);
        logger.info("Saved product: " + product.getProductName());
        System.out.println("--------------------");

    }

    private static String showAlternatives(Scanner scanner) {
        System.out.println("01 Add product");
        System.out.println("02 Add categories");
        System.out.println("03 List categories");
        System.out.println("04 List all products");
        System.out.println("05 List all products by category");
        System.out.println("Write !q to quit");
        return scanner.nextLine();
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
        InputStream input = new FileInputStream("src/main/resources/conf/application.properties");
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        Properties env = new Properties();

        env.load(input);

        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUser(env.getProperty("db.user"));
        dataSource.setPassword(env.getProperty("db.password"));
        logger.info("Authorizing...");

        boolean connectionEstablished = Flyway.configure().dataSource(dataSource).load().migrate().warnings.isEmpty();
        if (connectionEstablished) logger.info("Connected...");
        return dataSource;

    }
}
