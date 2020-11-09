package ru.mail.accounting;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.mail.accounting.db.tables.records.OrganizationRecord;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static ru.mail.accounting.db.tables.Organization.ORGANIZATION;

public final class OrganizationDAO implements DAO<Organization> {

    final Connection connection;

    public OrganizationDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Organization get(int id) {
        return null;
    }

    public Organization get(@NotNull String name) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);

        OrganizationRecord record = dslContext.selectFrom(ORGANIZATION)
                .where(ORGANIZATION.NAME.eq(name)).fetchOne();

        String name1 = record.getName();
        int INN = Math.toIntExact(record.getInn());
        int checkingAccount = Math.toIntExact(record.getCheckingAccount());

        return new Organization(name1, INN, checkingAccount);

    }

    @Override
    public List<Organization> getAll() {
        final List<Organization> result = new ArrayList<>();
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);

        Result<OrganizationRecord> records = dslContext
                .selectFrom(ORGANIZATION)
                .fetch();

        for (OrganizationRecord r : records) {
            result.add(new Organization(r.getName(), Math.toIntExact(r.getInn()), Math.toIntExact(r.getCheckingAccount())));
        }
        return result;
    }


    @Override
    public void save(Organization entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.insertInto(ORGANIZATION, ORGANIZATION.NAME, ORGANIZATION.INN, ORGANIZATION.CHECKING_ACCOUNT)
                .values(entity.getName(), (long) entity.getINN(), (long) entity.getCheckingAccount())
                .execute();
    }


    @Override
    public void update(Organization entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.update(ORGANIZATION)
                .set(ORGANIZATION.INN, (long) entity.getINN())
                .set(ORGANIZATION.CHECKING_ACCOUNT, (long) entity.getCheckingAccount())
                .where(ORGANIZATION.NAME.eq(entity.getName()))
                .execute();
    }

    @Override
    public void delete(Organization entity) {
        DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);
        dslContext.delete(ORGANIZATION)
                .where(ORGANIZATION.NAME.eq(entity.getName()))
                .execute();
    }
}
