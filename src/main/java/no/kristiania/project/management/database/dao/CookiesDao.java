package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.Cookie;
import no.kristiania.project.management.database.tabels.Project;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CookiesDao extends AbstractDao<Cookie> {
    public CookiesDao(DataSource dataSource) {
        super(dataSource);
    }

    public void insert(Cookie cookie) throws SQLException {
       long id = insert(cookie, "insert into cookies (cookie, workerID) values (?,?)");
       cookie.setID(id);
    }

    public List<Cookie> listAll() throws SQLException {
        return listAll("select * from cookies", -1);
    }

    @Override
    protected void mapToStatement(Cookie cookie, PreparedStatement statement) throws SQLException {
        statement.setString(1, cookie.getCookie());
        statement.setLong(2, cookie.getWorkerID());
    }

    @Override
    protected Cookie mapFromResultSet(ResultSet rs) throws SQLException {
        Cookie cookie = new Cookie();
        cookie.setWorkerID(rs.getLong("workerID"));
        cookie.setCookie(rs.getString("cookie"));
        cookie.setID(rs.getLong("id"));
        return cookie;
    }

    public Cookie retrieve(Long id) throws SQLException {
        return retrieve(id , "select * from cookies where id = ?");
    }

    public Cookie retrieve(String cookieValue) throws SQLException {
        return retrieve(cookieValue , "select * from cookies where cookie = ?");
    }

    //Get value of worker based on cookie value
    public Cookie retrieve(String cookieValue, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, cookieValue);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next())
                        return mapFromResultSet(rs);
                    else
                        return null;
                }
            }
        }
    }

    public void update(Cookie cookieToUpdate) throws SQLException {
        update(cookieToUpdate,
                "update cookies set workerID =(?) where cookie = (?)");
    }

    @Override
    protected void mapToUpdateStatement(Cookie cookie, PreparedStatement statement) throws SQLException {
        statement.setLong(1, cookie.getWorkerID());
        statement.setString(2, cookie.getCookie());
    }
}
