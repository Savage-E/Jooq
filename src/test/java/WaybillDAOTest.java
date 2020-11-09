import org.flywaydb.core.Flyway;
import org.junit.Test;
import ru.mail.accounting.Organization;
import ru.mail.accounting.OrganizationDAO;
import ru.mail.accounting.Waybill;
import ru.mail.accounting.WaybillDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WaybillDAOTest {
    @Test
    public void WaybillDAOTest() {
        final Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://127.0.0.1:5432/database1", "postgres", "")
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/database1", "postgres", "")) {

            OrganizationDAO organisationDAO = new OrganizationDAO(connection);
            organisationDAO.save(new Organization("Milkey", 3322, 323534));
            organisationDAO.save(new Organization("Adidas", 33232, 323));
            organisationDAO.save(new Organization("Nike", 333234, 3234));


            WaybillDAO dao = new WaybillDAO(connection);
            dao.save(new Waybill(1, LocalDate.of(2010, 1, 23), "Milkey"));
            dao.save(new Waybill(2, LocalDate.of(2010, 2, 24), "Nike"));
            dao.save(new Waybill(3, LocalDate.of(2010, 3, 13), "Adidas"));
            Waybill expected = new Waybill(1, LocalDate.of(2010, 1, 23), "Milkey");
            //Get  and save tests.
            Waybill actual = dao.get(1);
            assertEquals(expected.getWaybillNum(), actual.getWaybillNum());

            //Update test.
            dao.update(new Waybill(1, LocalDate.of(2010, 3, 23), "Milkey"));
            LocalDate expectedDate = LocalDate.of(2010, 3, 23);
            LocalDate actualDate = dao.get(1).getWaybillDate();
            assertEquals(expectedDate, actualDate);

            //delete test.
            dao.delete(new Waybill(1, LocalDate.of(2010, 1, 23), "Bounty"));
            assertThrows(NullPointerException.class,
                    () -> {
                        dao.get(1);
                    });
            //GetALL test.
            List<Waybill> List = dao.getAll();
            String actual1 = List.get(0).getOrgSender();
            String expected1 = "Nike";
            String expected2 = "Adidas";
            String actual2 = List.get(1).getOrgSender();
            assertEquals(expected1, actual1);
            assertEquals(expected2, actual2);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
