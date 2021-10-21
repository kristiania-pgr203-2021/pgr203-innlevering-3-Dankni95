package no.kristiania.person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDao extends AbstractDao<String>  {
    public CategoryDao(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    protected String rowToObject(ResultSet rs) throws SQLException {
        return rs.getString("category_name");
    }

    @Override
    protected Long rowToObjectId(ResultSet rs) throws SQLException {
        System.out.println(rs.getInt("id"));
        return rs.getLong("id");
    }


    public void save(String category) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into category (category_name) values (?)")) {
                statement.setString(1, category);
                statement.executeUpdate();
            }
        }
    }

    @Override
    public List<String> listAll() throws SQLException {
        return super.listAll("SELECT * FROM category");
    }

    public void deleteAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("delete from category")) {
                statement.executeUpdate();
            }
        }
    }


        public void getCategoryById(String categoryName) throws SQLException {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("select id from category where category_name = ?")) {
                    statement.setString(1, categoryName);
                    statement.executeUpdate();
                }
            }
    }
    public Long retrieveName(String categoryName) throws SQLException {
        return super.retrieveByName("SELECT id FROM category WHERE category_name = ?", categoryName);
    }
}
