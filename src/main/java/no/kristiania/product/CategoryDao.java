package no.kristiania.product;


import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class CategoryDao extends AbstractDao<Category> {
    public CategoryDao(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    protected Category rowToObject(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryName(rs.getString("category_name"));
        category.setId(rs.getLong("id"));
        return category;
    }


    public void save(Category category) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into category (category_name) values (?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, category.getCategoryName());
                statement.executeUpdate();
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    category.setId(rs.getLong("id"));
                }
            }
        }
    }

    protected Long retrieveCategoryIdByName(String name) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM category WHERE category_name = ?")) {
                statement.setString(1, name);
                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    return rs.getLong(1);
                }
            }
        }
    }

    @Override
    public List<Category> listAll() throws SQLException {
        return super.listAll("SELECT * FROM category");
    }

    
    public Category retrieve(long id) throws SQLException {
        return super.retrieve("SELECT * FROM category WHERE id =  ?", id);
    }
}
