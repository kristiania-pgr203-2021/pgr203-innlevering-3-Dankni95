package no.kristiania.product;

import no.kristiania.http.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDao extends AbstractDao<Product> {
    public ProductDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected Product rowToObject(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setProductName(rs.getString("product_name"));
        product.setPrice(rs.getInt("product_price"));
        product.setDescription(rs.getString("product_description"));
        product.setCategoryId(rs.getLong("category_id"));
        return product;
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

    protected List<Product> listProductsByCategory(String sql, Long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    List<Product> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(rowToObject(rs));
                    }
                    return result;
                }
            }
        }
    }

    public Product retrieve(long id) throws SQLException {
        return super.retrieve("SELECT * FROM product WHERE id =  ?", id);
    }


    @Override
    public List<Product> listAll() throws SQLException {
        return super.listAll("SELECT * FROM product");
    }

    public List<Product> listProductsByCategory(Long id) throws SQLException {
        return listProductsByCategory("SELECT * FROM product WHERE category_id =  ?", id);
    }


}
