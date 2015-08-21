package liquibase.sqlgenerator.ext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import liquibase.change.ColumnConfig;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.metadata.ColumnMetaData;
import liquibase.metadata.DatabaseMetaDataAccessor;
import liquibase.metadata.ForeignKeyConstraintMetaData;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.core.AddForeignKeyConstraintGenerator;
import liquibase.sqlgenerator.core.DropForeignKeyConstraintGenerator;
import liquibase.statement.core.AddForeignKeyConstraintStatement;
import liquibase.statement.core.DropForeignKeyConstraintStatement;

public class SqlGeneratorHelperHanaDB {

    public static String getColumnDataDefinition(Database database, 
            String catalogName, String schemaName, String tableName, String columnName) {
        assertNotNull(tableName, "No table name specified");
        assertNotNull(columnName, "No column name specified");
        JdbcConnection conn = (JdbcConnection) database.getConnection();
        assertNotNull(conn, "Could not retrieve a connection");
        ColumnMetaData columnData;
        try {
            columnData = DatabaseMetaDataAccessor.getColumnMetaData(conn, catalogName, schemaName, tableName, columnName);
        } catch (DatabaseException e) {
            throw new UnexpectedLiquibaseException(e);
        }
        assertNotNull(columnData, "Could not determine data type for column = '" + columnName + "' table = '" + tableName + 
                "' schema = '" + schemaName + "' catalog = '" + catalogName + '\'');

        String columnDefinition;
        if (columnData.requiresSizeForDefinition()) {
            columnDefinition = columnData.getTypeName() + '(' +  columnData.getColumnSize() + ')';
        } else {
            columnDefinition = columnData.getTypeName();
        }
        return columnDefinition;
    }

    public static Set<ForeignKeyConstraintMetaData> getAllForeignKeyConstraints(Database database, String schemaName,
            String tableName) {
        if (tableName == null) {
            return new HashSet<ForeignKeyConstraintMetaData>();
        }
        JdbcConnection conn = (JdbcConnection) database.getConnection();
        assertNotNull(conn, "Could not retrieve a connection");
        Set<ForeignKeyConstraintMetaData> constraints = null;
        try {
            constraints = DatabaseMetaDataAccessor.getForeignKeyConstraintsForTable(conn, null, schemaName, tableName);
            constraints.addAll(DatabaseMetaDataAccessor.getForeignKeyConstraintsReferencingTable(conn, null,
                    schemaName, tableName));
            return constraints;
        } catch (DatabaseException e) {
            throw new UnexpectedLiquibaseException(e);
        }
    }

    public static void addDropForeignKeyConstraintsStatements(List<Sql> sql, Database database, 
            Set<ForeignKeyConstraintMetaData> constraints) {
        for (ForeignKeyConstraintMetaData constraint : constraints) {
            DropForeignKeyConstraintStatement statement = new DropForeignKeyConstraintStatement(null, 
                    constraint.getForeignKeySchema(), constraint.getForeignKeyTable(), constraint.getForeignKeyName());
            sql.addAll(Arrays.asList(new DropForeignKeyConstraintGenerator().generateSql(statement, database, null)));
        }
    }

    public static void addCreateForeignKeyConstraintsStatements(List<Sql> sql, Database database,
            Set<ForeignKeyConstraintMetaData> constraints) {
        for (ForeignKeyConstraintMetaData constraint : constraints) {
            AddForeignKeyConstraintStatement statement = new AddForeignKeyConstraintStatement(
                    constraint.getForeignKeyName(), null, constraint.getForeignKeySchema(),
                    constraint.getForeignKeyTable(), ColumnConfig.arrayFromNames(constraint.getForeignKeyColumnsAsString()), null,
                    constraint.getPrimaryKeySchema(), constraint.getPrimaryKeyTable(),
                    ColumnConfig.arrayFromNames(constraint.getPrimaryKeyColumnsAsString())).setOnDelete(constraint.getDeleteRuleAsString())
                    .setOnUpdate(constraint.getUpdateRuleAsString());
            sql.addAll(Arrays.asList(new AddForeignKeyConstraintGenerator().generateSql(statement, database, null)));
        }
    }
    
    private static void assertNotNull(Object o, String errorMessage) {
        if (o == null) {
            throw new UnexpectedLiquibaseException(errorMessage);
        }
    }
}
