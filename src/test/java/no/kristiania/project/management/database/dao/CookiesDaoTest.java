package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.Cookie;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

import static no.kristiania.project.management.database.dao.WorkerDaoTest.createDataSource;
import static org.assertj.core.api.Assertions.assertThat;

public class CookiesDaoTest {
    private CookiesDao cookiesDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = createDataSource();//Reuse createDataSource from WorkerDaoTest
        cookiesDao = new CookiesDao(dataSource);
    }


    @Test
    public void shouldListSavedCookies() throws SQLException {
        Cookie cookie = randomCookie();
        cookiesDao.insert(cookie);

        assertThat(cookiesDao.listAll())
                .flatExtracting(Cookie::getCookie, Cookie::getWorkerID)
                .contains(cookie.getCookie(), cookie.getWorkerID());
    }

    private Cookie randomCookie() {
        Cookie cookie = new Cookie();
        cookie.setWorkerID(new Random().nextInt());
        cookie.setCookie("Set-cookie: sessionId=" + UUID.randomUUID());
        return cookie;
    }

    @Test
    void shouldRetrieveSavedCookie() throws SQLException {
        Cookie cookie = randomCookie();
        cookiesDao.insert(cookie);

        assertThat(cookie).hasNoNullFieldsOrProperties();
        assertThat(cookiesDao.retrieve(cookie.getID())).isEqualToComparingFieldByField(cookie);
    }
}
