package no.kristiania.project.management.database.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> {
    protected final DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long insert(T o, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                mapToStatement(o, statement);
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                return generatedKeys.getLong("id");
            }

        }
    }

    protected abstract void mapToStatement(T o, PreparedStatement statement) throws SQLException;

    //If id is negative we are not using ID in our statement
    public List<T> listAll(String sql, long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                if (id > 0) //If id is positive we are filtering based on unique value from database
                    statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    List<T> allProducts = new ArrayList<>();
                    while (rs.next()) {
                        allProducts.add(mapFromResultSet(rs));
                    }
                    return allProducts;
                }
            }
        }
    }

    protected abstract T mapFromResultSet(ResultSet rs) throws SQLException;

    public T retrieve(Long id, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next())
                        return mapFromResultSet(rs);
                    else
                        return null;
                }
            }
        }
    }


    protected void update(T objectUpdated, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                mapToUpdateStatement(objectUpdated, statement);
                statement.executeUpdate();
            }
        }
    }

    /**
     * If we want to use abstract DAO to update data we have to implement this method
     * We do not declare it abstract so that we don't have to implement it in DAOs where it is not used
     * @param o Object that is being updated
     * @param statement statement to update object
     * @throws SQLException can throw sql exception
     */
    protected void mapToUpdateStatement(T o, PreparedStatement statement) throws SQLException {
        throw new IllegalStateException();
    }
}