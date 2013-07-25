package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.sqlgenerator.core.RenameViewGenerator;
import liquibase.statement.core.RenameViewStatement;


public class RenameViewGeneratorHanaDB extends RenameViewGenerator {

    @Override
    public boolean supports(RenameViewStatement statement, Database database) {
        return !(database instanceof HanaDBDatabase);
    }

}
