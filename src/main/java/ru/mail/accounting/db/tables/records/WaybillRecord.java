/*
 * This file is generated by jOOQ.
 */
package ru.mail.accounting.db.tables.records;


import java.sql.Date;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;

import ru.mail.accounting.db.tables.Waybill;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.14.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WaybillRecord extends UpdatableRecordImpl<WaybillRecord> implements Record3<Integer, Date, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.waybill.waybill_num</code>.
     */
    public WaybillRecord setWaybillNum(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill.waybill_num</code>.
     */
    public Integer getWaybillNum() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.waybill.waybill_date</code>.
     */
    public WaybillRecord setWaybillDate(Date value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill.waybill_date</code>.
     */
    public Date getWaybillDate() {
        return (Date) get(1);
    }

    /**
     * Setter for <code>public.waybill.org_sender</code>.
     */
    public WaybillRecord setOrgSender(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill.org_sender</code>.
     */
    public String getOrgSender() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, Date, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Integer, Date, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Waybill.WAYBILL.WAYBILL_NUM;
    }

    @Override
    public Field<Date> field2() {
        return Waybill.WAYBILL.WAYBILL_DATE;
    }

    @Override
    public Field<String> field3() {
        return Waybill.WAYBILL.ORG_SENDER;
    }

    @Override
    public Integer component1() {
        return getWaybillNum();
    }

    @Override
    public Date component2() {
        return getWaybillDate();
    }

    @Override
    public String component3() {
        return getOrgSender();
    }

    @Override
    public Integer value1() {
        return getWaybillNum();
    }

    @Override
    public Date value2() {
        return getWaybillDate();
    }

    @Override
    public String value3() {
        return getOrgSender();
    }

    @Override
    public WaybillRecord value1(Integer value) {
        setWaybillNum(value);
        return this;
    }

    @Override
    public WaybillRecord value2(Date value) {
        setWaybillDate(value);
        return this;
    }

    @Override
    public WaybillRecord value3(String value) {
        setOrgSender(value);
        return this;
    }

    @Override
    public WaybillRecord values(Integer value1, Date value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WaybillRecord
     */
    public WaybillRecord() {
        super(Waybill.WAYBILL);
    }

    /**
     * Create a detached, initialised WaybillRecord
     */
    public WaybillRecord(Integer waybillNum, Date waybillDate, String orgSender) {
        super(Waybill.WAYBILL);

        setWaybillNum(waybillNum);
        setWaybillDate(waybillDate);
        setOrgSender(orgSender);
    }
}
