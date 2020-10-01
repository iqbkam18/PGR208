package no.kristiania.project.management.database.controllers.update;

import no.kristiania.project.management.database.dao.CookiesDao;
import no.kristiania.project.management.database.dao.TaskDao;
import no.kristiania.project.management.database.tabels.Cookie;
import no.kristiania.project.management.database.tabels.Task;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class CookieWorkerTaskUpdateController extends AbstractDatabaseUpdateController {
    private final CookiesDao cookiesDao;

    public CookieWorkerTaskUpdateController(CookiesDao cookiesDao) {
        this.cookiesDao = cookiesDao;
    }

    @Override
    protected void update(Map<String, String> query) throws SQLException {
        Cookie retrievedCookie = cookiesDao.retrieve(query.get("cookie"));
        retrievedCookie.setWorkerID(Long.parseLong(query.get("ID")));
        cookiesDao.update(retrievedCookie);
    }

    /**
     * Needed only for testing
     * @return Returns html as string representation
     */
    public String getBody() {
        return "";
    }
}
