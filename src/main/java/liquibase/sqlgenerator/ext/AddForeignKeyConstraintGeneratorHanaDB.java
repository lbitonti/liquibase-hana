package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.core.AddForeignKeyConstraintGenerator;
import liquibase.statement.core.AddForeignKeyConstraintStatement;


public class AddForeignKeyConstraintGeneratorHanaDB extends AddForeignKeyConstraintGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(AddForeignKeyConstraintStatement statement, Database database) {
        return !(database instanceof HanaDBDatabase);
    }

}