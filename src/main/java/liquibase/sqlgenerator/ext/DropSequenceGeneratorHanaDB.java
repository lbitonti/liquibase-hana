package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.PostgresDatabase;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.DropSequenceGenerator;
import liquibase.statement.core.DropSequenceStatement;

public class DropSequenceGeneratorHanaDB extends DropSequenceGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(DropSequenceStatement statement, Database database) {
        return database.supportsSequences();
    }

    public Sql[] generateSql(DropSequenceStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String sql = "DROP SEQUENCE " +
                database.escapeSequenceName(statement.getCatalogName(), statement.getSchemaName(), statement.getSequenceName());
        if (database instanceof PostgresDatabase) {
            sql += " CASCADE";
        }
        if (database instanceof DerbyDatabase) {
            sql += " RESTRICT";
        }
        return new Sql[] {
                new UnparsedSql(sql, getAffectedSequence(statement))
        };
    }
}
