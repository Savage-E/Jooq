import org.flywaydb.core.Flyway;
import org.junit.Test;
import ru.mail.accounting.Nomenclature;
import ru.mail.accounting.NomenclatureDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NomenclatureDAOTest {
    @Test
    public void NomenclatureDAOTest() {
        final Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://127.0.0.1:5432/database1", "postgres", "")
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/database1", "postgres", "")) {

            NomenclatureDAO dao = new NomenclatureDAO(connection);
            dao.save(new Nomenclature(1, "Twix", 323534));
            dao.save(new Nomenclature(2, "Sneaker", 323));
            dao.save(new Nomenclature(3, "Charger", 3234));
            Nomenclature expected = new Nomenclature(1, "Twix", 323534);
            //Get  and save tests.
            Nomenclature actual = dao.get(1);
            assertEquals(expected.getName(), actual.getName());

            //Update test.
            dao.update(new Nomenclature(1, "Mars", 323534));
            String expectedName = "Mars";
            String actualName = dao.get(1).getName();
            assertEquals(expectedName, actualName);

            //delete test.
            dao.delete(new Nomenclature(1, "Mars", 323534));
            assertThrows(NullPointerException.class,
                    () -> {
                        dao.get(1);
                    });
            //GetALL test.
            List<Nomenclature> List = dao.getAll();
            String actual1 = List.get(0).getName();
            String expected1 = "Sneaker";
            String expected2 = "Charger";
            String actual2 = List.get(1).getName();
            assertEquals(expected1, actual1);
            assertEquals(expected2, actual2);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
