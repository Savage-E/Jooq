package ru.mail.accounting;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.mail.accounting.db.tables.records.WaybillRecord;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.mail.accounting.db.tables.Waybill.WAYBILL;

public class WaybillDAO implements DAO<Waybill> {
    final Connection connection;

    public WaybillDAO(Connection connection) {
        this.connection = connection;
    }

    public Waybill get(int waybill_num) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);

        WaybillRecord record = dslContext.selectFrom(WAYBILL)
                .where(WAYBILL.WAYBILL_NUM.eq(waybill_num)).fetchOne();

        int num = record.getWaybillNum();
        LocalDate localDate = record.getWaybillDate().toLocalDate();
        String orgSender = record.getOrgSender();

        return new Waybill(num, localDate, orgSender);

    }

    @Override
    public List<Waybill> getAll() {
        final List<Waybill> result = new ArrayList<>();

        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);

        Result<WaybillRecord> records = dslContext
                .selectFrom(WAYBILL)
                .fetch();

        for (WaybillRecord r : records) {
            result.add(new Waybill(r.getWaybillNum(), r.getWaybillDate().toLocalDate(), r.getOrgSender()));
        }
        return result;

    }

    @Override
    public void save(Waybill entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);

        dslContext.insertInto(WAYBILL, WAYBILL.WAYBILL_NUM, WAYBILL.WAYBILL_DATE, WAYBILL.ORG_SENDER)
                .values(entity.getWaybillNum(), Date.valueOf(entity.getWaybillDate()), entity.getOrgSender())
                .execute();

    }


    @Override
    public void update(Waybill entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.update(WAYBILL)
                .set(WAYBILL.WAYBILL_DATE, Date.valueOf(entity.getWaybillDate()))
                .set(WAYBILL.ORG_SENDER, entity.getOrgSender())
                .where(WAYBILL.WAYBILL_NUM.eq(entity.getWaybillNum()))
                .execute();
    }

    @Override
    public void delete(Waybill entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.delete(WAYBILL)
                .where(WAYBILL.WAYBILL_NUM.eq(entity.getWaybillNum()))
                .execute();
    }


}
