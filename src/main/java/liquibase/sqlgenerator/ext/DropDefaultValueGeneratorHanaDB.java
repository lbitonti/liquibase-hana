package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.DropDefaultValueGenerator;
import liquibase.statement.core.DropDefaultValueStatement;


public class DropDefaultValueGeneratorHanaDB extends DropDefaultValueGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(DropDefaultValueStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public Sql[] generateSql(DropDefaultValueStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        if (!supports(statement, database)) {
            return sqlGeneratorChain.generateSql(statement, database);
        }

        String tableName = statement.getTableName();
        String columnName = statement.getColumnName();
        String catalogName = statement.getCatalogName();

        String schemaToUse = statement.getSchemaName();
        if (schemaToUse == null) {
            schemaToUse = database.getDefaultSchemaName();
        }

        String columnDataType = statement.getColumnDataType();
        if (columnDataType == null) {
            columnDataType = SqlGeneratorHelperHanaDB.getColumnDataDefinition((HanaDBDatabase) database, catalogName, schemaToUse, tableName, columnName);
        }
        String sql = "ALTER TABLE " + database.escapeTableName(catalogName, schemaToUse, tableName) +
                " ALTER (" + database.escapeColumnName(catalogName, schemaToUse, tableName, columnName) +
                " " + DataTypeFactory.getInstance().fromDescription(columnDataType, database).toDatabaseDataType(database) +
                " DEFAULT NULL)";
        return new Sql[]{
                new UnparsedSql(sql,
                        getAffectedColumn(statement))
        };
    }
}
