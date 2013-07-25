package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.core.DB2Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.database.structure.Column;
import liquibase.database.structure.Table;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AddAutoIncrementGenerator;
import liquibase.statement.core.AddAutoIncrementStatement;

public class AddAutoIncrementGeneratorHanaDB extends AddAutoIncrementGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(AddAutoIncrementStatement statement, Database database) {
        return !(database instanceof HanaDBDatabase);
    }

}