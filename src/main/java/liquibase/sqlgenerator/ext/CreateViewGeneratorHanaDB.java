package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.CreateViewGenerator;
import liquibase.statement.core.CreateViewStatement;

import java.util.ArrayList;
import java.util.List;


public class CreateViewGeneratorHanaDB extends CreateViewGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(CreateViewStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public ValidationErrors validate(CreateViewStatement createViewStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();

        validationErrors.checkRequiredField("viewName", createViewStatement.getViewName());
        validationErrors.checkRequiredField("selectQuery", createViewStatement.getSelectQuery());

        if (createViewStatement.isReplaceIfExists()) {
            validationErrors.checkDisallowedField("replaceIfExists", createViewStatement.isReplaceIfExists(), database, HanaDBDatabase.class);
        }

        return validationErrors;
    }

    @Override
    public Sql[] generateSql(CreateViewStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String createClause;

        List<Sql> sql = new ArrayList<Sql>();

        sql.add(new UnparsedSql("CREATE VIEW " + database.escapeViewName(statement.getSchemaName(), statement.getViewName()) +
                " AS " + statement.getSelectQuery()));

        return sql.toArray(new Sql[sql.size()]);
    }

}
