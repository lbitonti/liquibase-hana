package liquibase.sqlgenerator.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.metadata.ForeignKeyConstraintMetaData;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.DropColumnGenerator;
import liquibase.statement.core.DropColumnStatement;


public class DropColumnGeneratorHanaDB extends DropColumnGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(DropColumnStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }


    public Sql[] generateSql(DropColumnStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        if (!supports(statement, database)) {
            return sqlGeneratorChain.generateSql(statement, database);
        }

        String catalogName = statement.getCatalogName();
        String schemaName = statement.getSchemaName();
        if (schemaName == null) {
            schemaName = database.getDefaultSchemaName();
        }
        String tableName = statement.getTableName();
        String columnName = statement.getColumnName();

        List<Sql> returnSql = new ArrayList<Sql>();
        
        // drop both incoming and outgoing foreign key constraints before dropping column
        Set<ForeignKeyConstraintMetaData> constraints = SqlGeneratorHelperHanaDB.getAllForeignKeyConstraints(database, schemaName, tableName);
        SqlGeneratorHelperHanaDB.addDropForeignKeyConstraintsStatements(returnSql, database, constraints);
        
        returnSql.add(new UnparsedSql("ALTER TABLE " +
                database.escapeTableName(catalogName, schemaName, tableName) +
                " DROP (" +
                database.escapeColumnName(catalogName, schemaName, tableName, columnName) +
                ")", getAffectedColumn(statement)));

        // recreate foreign key constraints
        SqlGeneratorHelperHanaDB.addCreateForeignKeyConstraintsStatements(returnSql, database, constraints);

        return returnSql.toArray(new Sql[returnSql.size()]);
    }

}
