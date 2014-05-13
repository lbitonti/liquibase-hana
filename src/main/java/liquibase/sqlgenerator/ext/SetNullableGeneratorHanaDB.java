package liquibase.sqlgenerator.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.metadata.ForeignKeyConstraintMetaData;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.SetNullableGenerator;
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
        if (columnDataTypeName == null) {
            columnDataTypeName = SqlGeneratorHelperHanaDB.getColumnDataDefinition(database, catalogName, schemaToUse, tableName, columnName);
        }

        // drop both incoming and outgoing foreign key constraints before dropping notNull constraint
        Set<ForeignKeyConstraintMetaData> constraints = SqlGeneratorHelperHanaDB.getAllForeignKeyConstraints(database, schemaToUse, tableName);
        List<Sql> sqlStatements = new ArrayList<Sql>();
        SqlGeneratorHelperHanaDB.addDropForeignKeyConstraintsStatements(sqlStatements, database, constraints);

        String sql = "ALTER TABLE " + database.escapeTableName(catalogName, schemaToUse, tableName) +
                " ALTER (" + database.escapeColumnName(catalogName, schemaToUse, tableName, columnName) +
                " " + DataTypeFactory.getInstance().fromDescription(columnDataTypeName, database).toDatabaseDataType(database) + nullableString + ")";
        sqlStatements.add(new UnparsedSql(sql, getAffectedColumn(statement)));

        // recreate foreign key constraints
        SqlGeneratorHelperHanaDB.addCreateForeignKeyConstraintsStatements(sqlStatements, database, constraints);

        return sqlStatements.toArray(new Sql[sqlStatements.size()]);
    }

}
