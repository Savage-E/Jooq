import org.flywaydb.core.Flyway;
import org.junit.Test;
import ru.mail.accounting.Organization;
import ru.mail.accounting.OrganizationDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrganisationDAOTest {

    @Test
    public void OrganisationDAOTest() {
        final Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://127.0.0.1:5432/database1", "postgres", "")
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/database1", "postgres", "")) {

            OrganizationDAO organisationDAO = new OrganizationDAO(connection);
            organisationDAO.save(new Organization("Bublik", 3322, 323534));
            organisationDAO.save(new Organization("Adidas", 33232, 323));
            organisationDAO.save(new Organization("Nike", 333234, 3234));
            Organization expected = new Organization("Bublik", 3322, 323534);
            //Get  and save tests.
            Organization actual = organisationDAO.get("Bublik");
            assertEquals(expected.getName(), actual.getName());

            //Update test.
            organisationDAO.update(new Organization("Bublik", 33222, 323534));
            int expectedINN = 33222;
            int actualINN = organisationDAO.get("Bublik").getINN();
            assertEquals(expectedINN, actualINN);
            //delete test.
            organisationDAO.delete(new Organization("Bublik", 33222, 323534));
            assertThrows(NullPointerException.class,
                    () -> {
                        organisationDAO.get("Bublik");
                    });
            //GetALL test.
            List<Organization> List = organisationDAO.getAll();
            String actual1 = List.get(0).getName();
            String expected1 = "Adidas";
            String expected2 = "Nike";
            String actual2 = List.get(1).getName();
            assertEquals(expected1, actual1);
            assertEquals(expected2, actual2);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
