package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.SetNullableGenerator;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.SetNullableStatement;

public class SetNullableGeneratorHanaDB extends SetNullableGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(SetNullableStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public Sql[] generateSql(SetNullableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        if (!supports(statement, database)) {
            return sqlGeneratorChain.generateSql(statement, database);
        }

        String nullableString;
        if (statement.isNullable()) {
//            nullableString = " DEFAULT NULL";
            nullableString = " NULL";
        } else {
            nullableString = " NOT NULL";
        }

        String schemaToUse = statement.getSchemaName();
        if (schemaToUse == null) {
            schemaToUse = database.getDefaultSchemaName();
        }
        String columnDataTypeName = statement.getColumnDataType();
        String catalogName = statement.getCatalogName();
        String tableName = statement.getTableName();
        String columnName = statement.getColumnName();
        if (columnDataTypeName == null && schemaToUse != null) {
            columnDataTypeName = ((HanaDBDatabase) database).getColumnDataTypeName(catalogName, schemaToUse, tableName, columnName);
        }

        String sql = "ALTER TABLE " + database.escapeTableName(catalogName, schemaToUse, tableName) +
                " ALTER (" + database.escapeColumnName(catalogName, schemaToUse, tableName, columnName) +
                " " + DataTypeFactory.getInstance().fromDescription(columnDataTypeName, database).toDatabaseDataType(database) + nullableString + ")";

        return new Sql[] {
                new UnparsedSql(sql, getAffectedColumn(statement))
        };
    }

}
