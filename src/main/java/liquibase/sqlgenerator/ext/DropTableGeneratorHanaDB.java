package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.logging.LogFactory;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.sqlgenerator.core.DropTableGenerator;
import liquibase.statement.core.DropSequenceStatement;
import liquibase.statement.core.DropTableStatement;


public class DropTableGeneratorHanaDB extends DropTableGenerator {

    @Override
    public boolean supports(DropTableStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    public Sql[] generateSql(DropTableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DROP TABLE ")
              .append(database.escapeTableName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName()));
        if (statement.isCascadeConstraints()) {
            buffer.append(" CASCADE");
        }
//        else {
//            buffer.append(" RESTRICT");
//        }

        return new Sql[]{
                new UnparsedSql(buffer.toString(), getAffectedTable(statement))
        };
    }

}
