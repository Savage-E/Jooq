package ru.mail.accounting;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.mail.accounting.db.tables.records.NomenclatureRecord;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static ru.mail.accounting.db.tables.Nomenclature.NOMENCLATURE;

public final class NomenclatureDAO implements DAO<Nomenclature> {
    final Connection connection;

    public NomenclatureDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Nomenclature get(int id) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);

        NomenclatureRecord record = dslContext
                .selectFrom(NOMENCLATURE)
                .where(NOMENCLATURE.ID.eq(id))
                .fetchOne();

        int id1 = record.getId();
        String name1 = record.getName();
        int internal_code1 = Math.toIntExact(record.getInternalCode());

        return new Nomenclature(id1, name1, internal_code1);
    }

    @Override
    public List<Nomenclature> getAll() {
        final List<Nomenclature> result = new ArrayList<>();

        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        Result<NomenclatureRecord> records = dslContext
                .selectFrom(NOMENCLATURE)
                .fetch();
        for (NomenclatureRecord r : records) {
            result.add(new Nomenclature(r.getId(), r.getName(), Math.toIntExact(r.getInternalCode())));
        }
        return result;
    }

    @Override
    public void save(Nomenclature entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.insertInto(NOMENCLATURE, NOMENCLATURE.ID, NOMENCLATURE.NAME, NOMENCLATURE.INTERNAL_CODE)
                .values(entity.getId(), entity.getName(), (long) entity.getInternalCode())
                .execute();
    }


    @Override
    public void update(Nomenclature entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.update(NOMENCLATURE)
                .set(NOMENCLATURE.NAME, entity.getName())
                .set(NOMENCLATURE.INTERNAL_CODE, (long) entity.getInternalCode())
                .where(NOMENCLATURE.ID.eq(entity.getId()))
                .execute();
    }

    @Override
    public void delete(Nomenclature entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.delete(NOMENCLATURE)
                .where(NOMENCLATURE.ID.eq(entity.getId()))
                .execute();
    }
}
