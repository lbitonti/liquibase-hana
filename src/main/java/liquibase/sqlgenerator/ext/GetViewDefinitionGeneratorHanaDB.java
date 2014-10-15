package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
//import liquibase.database.core.MaxDBDatabase;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.DatabaseException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.GetViewDefinitionGenerator;
import liquibase.statement.core.GetViewDefinitionStatement;

public class GetViewDefinitionGeneratorHanaDB extends GetViewDefinitionGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(GetViewDefinitionStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public Sql[] generateSql(GetViewDefinitionStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        return new Sql[] {
                new UnparsedSql("SELECT DEFINITION FROM VIEWS WHERE upper(VIEW_NAME)='" + statement.getViewName().toUpperCase() + "'")
        };
    }

}