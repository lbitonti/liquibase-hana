package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.core.AddPrimaryKeyGenerator;
import liquibase.statement.core.AddPrimaryKeyStatement;
import liquibase.util.StringUtils;


public class AddPrimaryKeyGeneratorHanaDB extends AddPrimaryKeyGenerator {

    @Override
    public boolean supports(AddPrimaryKeyStatement statement, Database database) {
//        return database instanceof HanaDBDatabase;
        return !(database instanceof HanaDBDatabase);
    }

//    public Sql[] generateSql(AddPrimaryKeyStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
//        String sql;
//            sql = "ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) +
//                    " ALTER (" + database.escapeColumnNameList(statement.getColumnNames()) + " PRIMARY KEY)";
//
//        return new Sql[] {
//                new UnparsedSql(sql)
//        };
//    }

}
