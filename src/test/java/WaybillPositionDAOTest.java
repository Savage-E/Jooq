import org.flywaydb.core.Flyway;
import org.junit.Test;
import ru.mail.accounting.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WaybillPositionDAOTest {
    @Test
    public void WaybillPositionDaoTest() {
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

            WaybillDAO waybillDAO = new WaybillDAO(connection);
            waybillDAO.save(new Waybill(1, LocalDate.of(2010, 1, 23), "Milkey"));
            waybillDAO.save(new Waybill(2, LocalDate.of(2010, 2, 24), "Nike"));
            waybillDAO.save(new Waybill(3, LocalDate.of(2010, 3, 13), "Adidas"));

            NomenclatureDAO nomenclatureDAO = new NomenclatureDAO(connection);
            nomenclatureDAO.save(new Nomenclature(1, "Twix", 323534));
            nomenclatureDAO.save(new Nomenclature(2, "Sneaker", 323));
            nomenclatureDAO.save(new Nomenclature(3, "Charger", 3234));

            WaybillPositionDAO positionDAO = new WaybillPositionDAO(connection);

            positionDAO.save(new WaybillPosition(1, 2342, 1, 422, 1));
            positionDAO.save(new WaybillPosition(2, 234211, 2, 422, 2));
            positionDAO.save(new WaybillPosition(3, 234232, 3, 422, 3));


            WaybillPosition expected = new WaybillPosition(1, 2342, 1, 422, 1);
            //Get  and save tests.
            WaybillPosition actual = positionDAO.get(1);
            assertEquals(expected.getAmount(), actual.getAmount());

            //Update test.
            positionDAO.update(new WaybillPosition(1, 15000, 1, 422, 1));
            int expectedPrice = 15000;
            int actualPrice = positionDAO.get(1).getPrice();
            assertEquals(expectedPrice, actualPrice);

            //delete test.
            positionDAO.delete(new WaybillPosition(1, 15000, 1, 422, 1));
            assertThrows(NullPointerException.class,
                    () -> {
                        positionDAO.get(1);
                    });
            //GetALL test.
            List<WaybillPosition> List = positionDAO.getAll();
            int actual1 = List.get(0).getNomenclature();
            int expected1 = 2;
            int expected2 = 3;
            int actual2 = List.get(1).getNomenclature();
            assertEquals(expected1, actual1);
            assertEquals(expected2, actual2);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
