package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.sqlgenerator.core.DropViewGenerator;
import liquibase.statement.core.DropTableStatement;
import liquibase.statement.core.DropViewStatement;


public class DropViewGeneratorHanaDB extends DropViewGenerator {

    @Override
    public boolean supports(DropViewStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public Sql[] generateSql(DropViewStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        return new Sql[] {
                new UnparsedSql("DROP VIEW " + database.escapeViewName(statement.getSchemaName(), statement.getViewName()))
        };
    }

}
