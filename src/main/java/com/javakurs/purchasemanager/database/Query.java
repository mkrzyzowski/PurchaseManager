package com.javakurs.purchasemanager.database;

/**
 * Obiekt zawierający własne elementy zapytania do bazy danych.
 */
public class Query {
    private String sqlFrom;
    private String sqlFields;
    private String sqlWhere;
    private String sqlOrderBy;

    public Query() {
    }

    public Query(String sqlFrom, String sqlFields, String customSqlWhere, String sqlOrderBy) {
        this.sqlFrom = sqlFrom;
        this.sqlFields = sqlFields;
        this.sqlWhere = customSqlWhere;
        this.sqlOrderBy = sqlOrderBy;
    }

    public String getSqlFrom() {
        return sqlFrom;
    }

    public void setSqlFrom(String sqlFrom) {
        this.sqlFrom = sqlFrom;
    }

    public String getSqlFields() {
        return sqlFields;
    }

    public void setSqlFields(String sqlFields) {
        this.sqlFields = sqlFields;
    }

    public String getSqlWhere() {
        return sqlWhere;
    }

    public void setSqlWhere(String sqlWhere) {
        this.sqlWhere = sqlWhere;
    }

    public String getSqlOrderBy() {
        return sqlOrderBy;
    }

    public void setSqlOrderBy(String sqlOrderBy) {
        this.sqlOrderBy = sqlOrderBy;
    }
}
