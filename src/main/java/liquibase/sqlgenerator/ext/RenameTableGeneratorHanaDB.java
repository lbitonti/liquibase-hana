package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.RenameTableGenerator;
import liquibase.statement.core.RenameTableStatement;


public class RenameTableGeneratorHanaDB extends RenameTableGenerator {

    @Override
    public boolean supports(RenameTableStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public ValidationErrors validate(RenameTableStatement renameTableStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("newTableName", renameTableStatement.getNewTableName());
        validationErrors.checkRequiredField("oldTableName", renameTableStatement.getOldTableName());
        return validationErrors;
    }

    @Override
    public Sql[] generateSql(RenameTableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String sql;

        sql = "RENAME TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getOldTableName()) +
                " TO " + database.escapeDatabaseObject(statement.getNewTableName());

        return new Sql[] {
                new UnparsedSql(sql)
        };
    }

}
