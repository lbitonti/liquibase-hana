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
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(DropUniqueConstraintStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public Sql[] generateSql(DropUniqueConstraintStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        if (!supports(statement, database)) {
            return sqlGeneratorChain.generateSql(statement, database);
        }

        String sql = "ALTER TABLE " + database.escapeTableName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName()) +
                " DROP CONSTRAINT " + database.escapeConstraintName(statement.getConstraintName());

        return new Sql[] {
                new UnparsedSql(sql, getAffectedUniqueConstraint(statement))
        };
    }
}
