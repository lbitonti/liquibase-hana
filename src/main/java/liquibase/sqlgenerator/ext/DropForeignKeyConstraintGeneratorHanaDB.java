package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.core.DropForeignKeyConstraintGenerator;
import liquibase.statement.core.DropForeignKeyConstraintStatement;


public class DropForeignKeyConstraintGeneratorHanaDB extends DropForeignKeyConstraintGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(DropForeignKeyConstraintStatement statement, Database database) {
        return !(database instanceof HanaDBDatabase);
    }

}