package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.DropUniqueConstraintGenerator;
import liquibase.statement.core.DropUniqueConstraintStatement;

public class DropUniqueConstraintGeneratorHanaDB extends DropUniqueConstraintGenerator {

    @Override
    public boolean supports(DropUniqueConstraintStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public Sql[] generateSql(DropUniqueConstraintStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String sql;
            sql = "DROP INDEX " + database.escapeConstraintName(statement.getConstraintName());

        return new Sql[] {
                new UnparsedSql(sql, getAffectedUniqueConstraint(statement))
        };
    }
}
