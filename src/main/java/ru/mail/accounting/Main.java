package ru.mail.accounting;

import org.flywaydb.core.Flyway;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.sum;
import static ru.mail.accounting.db.tables.Nomenclature.NOMENCLATURE;
import static ru.mail.accounting.db.tables.Waybill.WAYBILL;
import static ru.mail.accounting.db.tables.WaybillPosition.WAYBILL_POSITION;

public class Main {

    public static void main(String[] args) {
        final Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://127.0.0.1:5432/database1", "postgres", "")
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();


        //SQL queries.
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/database1", "postgres", "")) {
            System.out.println("Connection OK.");
            OrganizationDAO org = new OrganizationDAO(connection);


            //Insert data into Organization


            org.save(new Organization("Magnit", 34, 432432451));
            org.save(new Organization("Patyorochka", 344314, 43244451));
            org.save(new Organization("Centre", 34214314, 324322451));
            org.save(new Organization("Perekrestok", 342143134, 4322451));
            org.save(new Organization("Xiaomi", 3342432, 434222451));
            org.save(new Organization("adidas", 34314, 43422142));
            org.save(new Organization("FixPrice", 7414, 434323243));
            org.save(new Organization("Nike", 2177314, 43422621));
            org.save(new Organization("Puma", 341774314, 4344241));
            org.save(new Organization("Billa", 34214, 4241));
            org.save(new Organization("Mail", 34543314, 434222));
            org.save(new Organization("Yandex", 3424111, 434224341));


            //Into Waybill.
            WaybillDAO waybill = new WaybillDAO(connection);

            waybill.save(new Waybill(1, LocalDate.of(2010, 04, 01), "Magnit"));
            waybill.save(new Waybill(2, LocalDate.of(2010, 05, 01), "Patyorochka"));
            waybill.save(new Waybill(3, LocalDate.of(2010, 06, 01), "Perekrestok"));
            waybill.save(new Waybill(4, LocalDate.of(2010, 07, 01), "Centre"));
            waybill.save(new Waybill(5, LocalDate.of(2010, 8, 01), "Xiaomi"));
            waybill.save(new Waybill(6, LocalDate.of(2010, 9, 01), "adidas"));
            waybill.save(new Waybill(7, LocalDate.of(2010, 10, 01), "FixPrice"));
            waybill.save(new Waybill(8, LocalDate.of(2010, 11, 01), "Nike"));
            waybill.save(new Waybill(9, LocalDate.of(2010, 12, 01), "Puma"));
            waybill.save(new Waybill(10, LocalDate.of(2011, 01, 01), "Billa"));
            waybill.save(new Waybill(11, LocalDate.of(2011, 02, 01), "Mail"));
            waybill.save(new Waybill(12, LocalDate.of(2011, 03, 01), "Yandex"));


            //Insert into Nomenclature.
            NomenclatureDAO nomenclatureDAO = new NomenclatureDAO(connection);

            nomenclatureDAO.save(new Nomenclature(1, "Milk", 2242));
            nomenclatureDAO.save(new Nomenclature(2, "Sneaker", 24432));
            nomenclatureDAO.save(new Nomenclature(3, "Smart_Station", 3224));

            //Into WayBill_position.

            WaybillPositionDAO waybillPosition = new WaybillPositionDAO(connection);

            waybillPosition.save(new WaybillPosition(1, 540000, 1, 40000, 1));
            waybillPosition.save(new WaybillPosition(2, 550000, 1, 41000, 2));
            waybillPosition.save(new WaybillPosition(3, 530000, 1, 44000, 3));
            waybillPosition.save(new WaybillPosition(4, 540000, 1, 42000, 4));
            waybillPosition.save(new WaybillPosition(5, 520000, 1, 44000, 7));
            waybillPosition.save(new WaybillPosition(6, 560000, 1, 40000, 10));

            waybillPosition.save(new WaybillPosition(7, 1000000, 2, 5200, 6));
            waybillPosition.save(new WaybillPosition(8, 1200000, 2, 5100, 8));
            waybillPosition.save(new WaybillPosition(9, 1400000, 2, 5400, 9));

            waybillPosition.save(new WaybillPosition(10, 500000, 3, 11000, 5));
            waybillPosition.save(new WaybillPosition(11, 510000, 3, 13200, 12));
            waybillPosition.save(new WaybillPosition(12, 490000, 3, 13100, 11));


            System.out.println("Report 1: Top 10 suppliers of the number of delivered goods ");

            DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
            Result<Record1<String>> result1 = dslContext.select(WAYBILL.ORG_SENDER)
                    .from(WAYBILL)
                    .where(WAYBILL.WAYBILL_NUM.in(((dslContext.select(WAYBILL_POSITION.WAYBILL.cast(Integer.TYPE))
                            .from(WAYBILL_POSITION)
                            .orderBy(WAYBILL_POSITION.AMOUNT.desc()).limit(10)))))
                    .fetch();

            for (Record r : result1) {
                System.out.println(r);
            }


            System.out.println("Report 2: suppliers of the number of delivered goods greater than 42000");

            Result<Record1<String>> result2 = dslContext.select(WAYBILL.ORG_SENDER)
                    .from(WAYBILL)
                    .where(WAYBILL.WAYBILL_NUM.in((dslContext.select(WAYBILL_POSITION.WAYBILL.cast(Integer.TYPE))
                            .from(WAYBILL_POSITION)
                            .where(WAYBILL_POSITION.AMOUNT.gt((long) 42000)))))
                    .fetch();


            for (Record r : result2) {
                System.out.println(r);
            }


            System.out.println("Report 3: quantity and amount of goods received in the specified period ");

            Result<Record2<BigDecimal, BigDecimal>> result3 = dslContext.select(sum(WAYBILL_POSITION.AMOUNT).as("total_amount"), sum(WAYBILL_POSITION.PRICE).as("total_price"))
                    .from(WAYBILL_POSITION)
                    .where(WAYBILL_POSITION.WAYBILL.in((dslContext.select(WAYBILL.WAYBILL_NUM.cast(Long.TYPE))
                            .from(WAYBILL)
                            .where(WAYBILL.WAYBILL_DATE.between(Date.valueOf("2010-04-01")).and(Date.valueOf("2010-07-01"))))))
                    .fetch();

            for (Record r : result3) {
                System.out.println(r);
            }


            System.out.println("Report 4: average price of goods for the received period ");

            Result<Record1<BigDecimal>> result4 = dslContext.select(avg(WAYBILL_POSITION.PRICE))
                    .from(WAYBILL_POSITION)
                    .where(WAYBILL_POSITION.WAYBILL.in((dslContext.select(WAYBILL.WAYBILL_NUM.cast(Long.TYPE))
                            .from(WAYBILL)
                            .where(WAYBILL.WAYBILL_DATE.between(Date.valueOf("2010-04-01")).and(Date.valueOf("2010-07-01"))))))
                    .fetch();

            for (Record r : result4) {
                System.out.println(r);
            }

            System.out.println("Report 5: list of goods supplied by organizations for the period ");

            Result<Record2<String, String>> result5 = dslContext.select(NOMENCLATURE.NAME, WAYBILL.ORG_SENDER)
                    .from(WAYBILL)
                    .join(WAYBILL_POSITION).on(WAYBILL.WAYBILL_NUM.eq((WAYBILL_POSITION.WAYBILL.cast(Integer.TYPE))))
                    .join(NOMENCLATURE).on(WAYBILL_POSITION.NOMENCLATURE.eq(NOMENCLATURE.ID.cast(Long.TYPE)))
                    .where(WAYBILL.WAYBILL_DATE.between(Date.valueOf("2010-04-01")).and(Date.valueOf("2011-03-01")))
                    .fetch();

            for (Record r : result5
            ) {
                System.out.println(r);
            }

        } catch (SQLException throwables) {
            System.out.println("Connection failure");
            throwables.printStackTrace();
        }
    }

}


