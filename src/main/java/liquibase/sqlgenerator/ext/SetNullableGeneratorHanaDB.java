package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.database.typeconversion.TypeConverter;
import liquibase.database.typeconversion.TypeConverterFactory;
import liquibase.exception.DatabaseException;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.sqlgenerator.core.SetNullableGenerator;
import liquibase.statement.core.ReorganizeTableStatement;
import liquibase.statement.core.SetNullableStatement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        validationErrors.checkRequiredField("columnDataType", setNullableStatement.getColumnDataType());

        return validationErrors;
    }

    @Override
    public Sql[] generateSql(SetNullableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String sql;

        TypeConverter typeConverter = TypeConverterFactory.getInstance().findTypeConverter(database);

        String nullableString;
        if (statement.isNullable()) {
            nullableString = " NULL";
        } else {
            nullableString = " NOT NULL";
        }

        sql = "ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) +
                " ALTER (" + database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName()) +
                " " + typeConverter.getDataType(statement.getColumnDataType(), false) + nullableString + ")";

        return new Sql[] {
                new UnparsedSql(sql)
        };
    }

}
