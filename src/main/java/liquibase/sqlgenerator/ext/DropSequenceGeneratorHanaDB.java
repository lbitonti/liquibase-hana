package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.sqlgenerator.core.DropSequenceGenerator;
import liquibase.statement.core.DropSequenceStatement;


public class DropSequenceGeneratorHanaDB extends DropSequenceGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(DropSequenceStatement statement, Database database) {
//        return database.supportsSequences();
        return database instanceof HanaDBDatabase;
    }

    public Sql[] generateSql(DropSequenceStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String sql = "DROP SEQUENCE " + database.escapeSequenceName(statement.getSchemaName(), statement.getSequenceName());
        // NOTE: Liquibase 2.0.x does not support CASCADE or RESTRICT options (they are supported in Hana).
        // If none of the 2 options is specified dependent objects will be invalidated but not dropped
        return new Sql[] {
                new UnparsedSql(sql)
        };
    }
}
