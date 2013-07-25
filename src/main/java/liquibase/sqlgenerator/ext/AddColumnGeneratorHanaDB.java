package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.SybaseASADatabase;
import liquibase.database.core.SybaseDatabase;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.database.structure.Column;
import liquibase.database.structure.Table;
import liquibase.database.typeconversion.TypeConverterFactory;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AddColumnGenerator;
import liquibase.statement.AutoIncrementConstraint;
import liquibase.statement.core.AddColumnStatement;

import java.util.ArrayList;
import java.util.List;


public class AddColumnGeneratorHanaDB extends AddColumnGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(AddColumnStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public Sql[] generateSql(AddColumnStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {

        String alterTable = null;

        if ( database instanceof HanaDBDatabase ) {
            alterTable = "ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) +
                    " ADD (" + database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName()) +
                    " " + TypeConverterFactory.getInstance().findTypeConverter(database).getDataType(statement.getColumnType(), statement.isAutoIncrement());
        }
        else {
            alterTable = "ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) +
                    " ADD " + database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName()) +
                    " " + TypeConverterFactory.getInstance().findTypeConverter(database).getDataType(statement.getColumnType(), statement.isAutoIncrement());
        }

        if (statement.isAutoIncrement() && database.supportsAutoIncrement()) {
            AutoIncrementConstraint autoIncrementConstraint = statement.getAutoIncrementConstraint();
            alterTable += " " + database.getAutoIncrementClause(autoIncrementConstraint.getStartWith(), autoIncrementConstraint.getIncrementBy());
        }

        if (!statement.isNullable()) {
            alterTable += " NOT NULL";
        } else {
            if (database instanceof SybaseDatabase || database instanceof SybaseASADatabase) {
                alterTable += " NULL";
            }
        }

        if (statement.isPrimaryKey()) {
            alterTable += " PRIMARY KEY";
        }

        if (statement.isUnique()) {
            alterTable += " UNIQUE ";
        }

        alterTable += getDefaultClause(statement, database);

        if ( database instanceof HanaDBDatabase ) {
            alterTable += ")";
        }

        List<Sql> returnSql = new ArrayList<Sql>();
        returnSql.add(new UnparsedSql(alterTable, new Column()
                .setTable(new Table(statement.getTableName()).setSchema(statement.getSchemaName()))
                .setName(statement.getColumnName())));

        addForeignKeyStatements(statement, database, returnSql);

        return returnSql.toArray(new Sql[returnSql.size()]);
    }


    private String getDefaultClause(AddColumnStatement statement, Database database) {
        String clause = "";
        Object defaultValue = statement.getDefaultValue();
        if (defaultValue != null) {
            if (database instanceof MSSQLDatabase) {
                clause += " CONSTRAINT " + ((MSSQLDatabase) database).generateDefaultConstraintName(statement.getTableName(), statement.getColumnName());
            }
            clause += " DEFAULT " + TypeConverterFactory.getInstance().findTypeConverter(database).getDataType(defaultValue).convertObjectToString(defaultValue, database);
        }
        return clause;
    }

}
