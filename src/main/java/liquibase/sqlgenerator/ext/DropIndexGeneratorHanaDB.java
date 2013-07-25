package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.database.structure.Index;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.sqlgenerator.core.DropIndexGenerator;
import liquibase.statement.core.CreateIndexStatement;
import liquibase.statement.core.DropIndexStatement;
import liquibase.util.StringUtils;

import java.util.List;

public class DropIndexGeneratorHanaDB extends DropIndexGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(DropIndexStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }


    @Override
    public Sql[] generateSql(DropIndexStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        List<String> associatedWith = StringUtils.splitAndTrim(statement.getAssociatedWith(), ",");
        if (associatedWith != null) {
            if (associatedWith.contains(Index.MARK_PRIMARY_KEY)|| associatedWith.contains(Index.MARK_UNIQUE_CONSTRAINT) ||
                    associatedWith.contains(Index.MARK_FOREIGN_KEY)) {
                return new Sql[0];
            }
        }

        String schemaName = statement.getTableSchemaName();
        
        return new Sql[] {new UnparsedSql("DROP INDEX " + database.escapeIndexName(schemaName, statement.getIndexName())) };
    }
}
