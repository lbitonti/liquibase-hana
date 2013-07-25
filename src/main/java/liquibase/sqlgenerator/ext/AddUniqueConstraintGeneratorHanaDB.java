package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AddUniqueConstraintGenerator;
import liquibase.statement.core.AddPrimaryKeyStatement;
import liquibase.statement.core.AddUniqueConstraintStatement;


public class AddUniqueConstraintGeneratorHanaDB extends AddUniqueConstraintGenerator {

    @Override
    public boolean supports(AddUniqueConstraintStatement statement, Database database) {
//        return database instanceof HanaDBDatabase;
        return !(database instanceof HanaDBDatabase);
    }

//    public Sql[] generateSql(AddPrimaryKeyStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
//        String sql;
//        sql = "ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) +
//                " ALTER (" + database.escapeColumnNameList(statement.getColumnNames()) + " UNIQUE)";
//
//        return new Sql[] {
//                new UnparsedSql(sql)
//        };
//    }

}
