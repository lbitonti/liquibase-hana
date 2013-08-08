package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.SetNullableGenerator;
import liquibase.statement.core.SetNullableStatement;

public class SetNullableGeneratorHanaDB extends SetNullableGenerator {

    @Override
    public boolean supports(SetNullableStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

	@Override
    public ValidationErrors validate(SetNullableStatement setNullableStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();

        validationErrors.checkRequiredField("tableName", setNullableStatement.getTableName());
        validationErrors.checkRequiredField("columnName", setNullableStatement.getColumnName());

        return validationErrors;
    }

    @Override
    public Sql[] generateSql(SetNullableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String sql;

        String nullableString;
        if (statement.isNullable()) {
//            nullableString = " DEFAULT NULL";
            nullableString = " NULL";
        } else {
            nullableString = " NOT NULL";
        }

        sql = "ALTER TABLE " + database.escapeTableName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName()) + 
        		" ALTER (" + database.escapeColumnName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName(), statement.getColumnName()) + 
        		" " + statement.getColumnDataType() + nullableString + ")";

        return new Sql[] {
                new UnparsedSql(sql, getAffectedColumn(statement))
        };
    }

}
