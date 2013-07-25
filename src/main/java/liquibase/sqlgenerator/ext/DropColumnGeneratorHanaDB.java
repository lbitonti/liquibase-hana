package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
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
        return new Sql[] { new UnparsedSql("ALTER TABLE " +
                database.escapeTableName(statement.getSchemaName(), statement.getTableName()) + " DROP (" +
                database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName()) + ")")
        };
    }

}
