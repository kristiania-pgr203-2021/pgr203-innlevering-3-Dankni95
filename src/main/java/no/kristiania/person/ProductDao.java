package no.kristiania.person;

import no.kristiania.http.Category;
import no.kristiania.http.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ProductDao extends AbstractDao<Product> {
    public ProductDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected Product rowToObject(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        return product;
    }

    @Override
    protected Long rowToObjectId(ResultSet rs) throws SQLException {
        return null;
    }

    public void save(Product product) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into product (product_name, product_description, product_price, category_id) values (?, ?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, product.getProductName());
                statement.setString(2, product.getDescription());
                statement.setInt(3, product.getPrice());
                statement.setLong(4, product.getCategoryId());

                statement.executeUpdate();
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    product.setId(rs.getLong("id"));
                }
            }
        }
    }

    public Product retrieve(long id) throws SQLException {
        return super.retrieve("SELECT * FROM people WHERE  = ?", id);
    }

    @Override
    public List<Product> listAll() throws SQLException {
        return super.listAll("SELECT * FROM people");
    }
}
