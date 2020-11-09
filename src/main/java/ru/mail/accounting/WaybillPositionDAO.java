package ru.mail.accounting;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.mail.accounting.db.tables.records.WaybillPositionRecord;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static ru.mail.accounting.db.tables.WaybillPosition.WAYBILL_POSITION;

public class WaybillPositionDAO implements DAO<WaybillPosition> {
    final Connection connection;

    public WaybillPositionDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public WaybillPosition get(int position) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);

        WaybillPositionRecord record = dslContext.selectFrom(WAYBILL_POSITION)
                .where(WAYBILL_POSITION.POSITION.eq(position)).fetchOne();

        int position1 = record.getPosition();
        int price = Math.toIntExact(record.getPrice());
        int nomenclature = Math.toIntExact(record.getNomenclature());
        int amount = Math.toIntExact(record.getAmount());
        int waybill = Math.toIntExact(record.getWaybill());

        return new WaybillPosition(position1, price, nomenclature, amount, waybill);
    }


    @Override
    public List<WaybillPosition> getAll() {
        final List<WaybillPosition> result = new ArrayList<>();

        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);

        Result<WaybillPositionRecord> records = dslContext
                .selectFrom(WAYBILL_POSITION)
                .fetch();
        for (WaybillPositionRecord r : records) {
            result.add(new WaybillPosition(r.getPosition(), Math.toIntExact(r.getPrice()), Math.toIntExact(r.getNomenclature()), Math.toIntExact(r.getAmount()), Math.toIntExact(r.getWaybill())));
        }
        return result;
    }

    @Override
    public void save(WaybillPosition entity) {

        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);

        dslContext.insertInto(WAYBILL_POSITION, WAYBILL_POSITION.POSITION, WAYBILL_POSITION.PRICE, WAYBILL_POSITION.NOMENCLATURE, WAYBILL_POSITION.AMOUNT, WAYBILL_POSITION.WAYBILL)
                .values(entity.getPosition(), (long) entity.getPrice(), (long) entity.getNomenclature(), (long) entity.getAmount(), (long) entity.getWaybill())
                .execute();
    }


    @Override
    public void update(WaybillPosition entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.update(WAYBILL_POSITION)
                .set(WAYBILL_POSITION.PRICE, (long) entity.getPrice())
                .set(WAYBILL_POSITION.NOMENCLATURE, (long) entity.getNomenclature())
                .set(WAYBILL_POSITION.AMOUNT, (long) entity.getAmount())
                .set(WAYBILL_POSITION.WAYBILL, (long) entity.getWaybill())
                .where(WAYBILL_POSITION.POSITION.eq(entity.getPosition()))
                .execute();
    }

    @Override
    public void delete(WaybillPosition entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.delete(WAYBILL_POSITION)
                .where(WAYBILL_POSITION.POSITION.eq(entity.getPosition()))
                .execute();
    }
}
