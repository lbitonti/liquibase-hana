package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AlterSequenceGenerator;
import liquibase.statement.core.AlterSequenceStatement;


public class AlterSequenceGeneratorHanaDB extends AlterSequenceGenerator {

    @Override
    public boolean supports(AlterSequenceStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public ValidationErrors validate(AlterSequenceStatement alterSequenceStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();

        validationErrors.checkDisallowedField("ordered", alterSequenceStatement.getOrdered(), database, HanaDBDatabase.class);

        validationErrors.checkRequiredField("sequenceName", alterSequenceStatement.getSequenceName());

        return validationErrors;
    }

    @Override
    public Sql[] generateSql(AlterSequenceStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ALTER SEQUENCE ");
        buffer.append(database.escapeSequenceName(statement.getSchemaName(), statement.getSequenceName()));

        if (statement.getIncrementBy() != null) {
            buffer.append(" INCREMENT BY ").append(statement.getIncrementBy());
        }

        if (statement.getMinValue() != null) {
//            buffer.append(" RESTART WITH ").append(statement.getMinValue());
            buffer.append(" MINVALUE ").append(statement.getMinValue());
        }

        if (statement.getMaxValue() != null) {
            buffer.append(" MAXVALUE ").append(statement.getMaxValue());
        }

        return new Sql[]{
                new UnparsedSql(buffer.toString())
        };
    }

}
