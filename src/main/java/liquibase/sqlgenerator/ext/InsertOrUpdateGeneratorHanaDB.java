package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.InsertOrUpdateGenerator;
import liquibase.statement.DatabaseFunction;
import liquibase.statement.core.InsertOrUpdateStatement;

import java.util.Date;
import java.util.Set;

/**
 * Created by Benjamin on 19. okt. 2016.
 */
public class InsertOrUpdateGeneratorHanaDB extends InsertOrUpdateGenerator {

    @Override
    public boolean supports(InsertOrUpdateStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public Sql[] generateSql(InsertOrUpdateStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        if (!supports(statement, database)) {
            return sqlGeneratorChain.generateSql(statement, database);
        }

        StringBuffer buffer = new StringBuffer();
        buffer
                .append("UPSERT ")
                .append(database.escapeTableName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName()))
                .append(" (");

        Set<String> columns = statement.getColumnValues().keySet();
        for (String column : columns) {
            buffer
                    .append(database.escapeColumnName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName(), column))
                    .append(", ");
        }

        buffer.deleteCharAt(buffer.lastIndexOf(" "));
        int lastComma = buffer.lastIndexOf(",");
        if (lastComma >= 0) {
            buffer.deleteCharAt(lastComma);
        }

        buffer
                .append(") ")
                .append("VALUES (");

        for (String column : columns) {
            buffer
                    .append(convertToString(statement.getColumnValue(column), database))
                    .append(", ");
        }

        buffer.deleteCharAt(buffer.lastIndexOf(" "));
        lastComma = buffer.lastIndexOf(",");
        if (lastComma >= 0) {
            buffer.deleteCharAt(lastComma);
        }

        buffer
                .append(") ")
                .append("WITH PRIMARY KEY");

        String sql = buffer.toString();

        return new Sql[] { new UnparsedSql(sql, getAffectedTable(statement)) };
    }

    @Override
    protected String getRecordCheck(InsertOrUpdateStatement statement, Database database, String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getElse(Database database) {
        throw new UnsupportedOperationException();
    }

    private String convertToString(Object newValue, Database database) {
        String sqlString;
        if (newValue == null || newValue.toString().equalsIgnoreCase("NULL")) {
            sqlString = "NULL";
        } else if (newValue instanceof String && !looksLikeFunctionCall(((String) newValue), database)) {
            sqlString = DataTypeFactory.getInstance().fromObject(newValue, database).objectToSql(newValue, database);
        } else if (newValue instanceof Date) {
            // converting java.util.Date to java.sql.Date
            Date date = (Date) newValue;
            if (date.getClass().equals(java.util.Date.class)) {
                date = new java.sql.Date(date.getTime());
            }

            sqlString = database.getDateLiteral(date);
        } else if (newValue instanceof Boolean) {
            if (((Boolean) newValue)) {
                sqlString = DataTypeFactory.getInstance().getTrueBooleanValue(database);
            } else {
                sqlString = DataTypeFactory.getInstance().getFalseBooleanValue(database);
            }
        } else if (newValue instanceof DatabaseFunction) {
            sqlString = database.generateDatabaseFunctionValue((DatabaseFunction) newValue);
        } else {
            sqlString = newValue.toString();
        }
        return sqlString;
    }
}
