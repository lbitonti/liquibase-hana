package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.database.structure.Column;
import liquibase.database.structure.Table;
import liquibase.database.typeconversion.TypeConverter;
import liquibase.database.typeconversion.TypeConverterFactory;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AddDefaultValueGenerator;
import liquibase.statement.core.AddDefaultValueStatement;


public class AddDefaultValueGeneratorHanaDB extends AddDefaultValueGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(AddDefaultValueStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public Sql[] generateSql(AddDefaultValueStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        Object defaultValue = statement.getDefaultValue();
        TypeConverter typeConverter = TypeConverterFactory.getInstance().findTypeConverter(database);

        String sql = "ALTER TABLE " + database.escapeTableName(statement.getSchemaName(), statement.getTableName()) +
                " ALTER (" + database.escapeColumnName(statement.getSchemaName(), statement.getTableName(), statement.getColumnName()) +
                " " + typeConverter.getDataType(statement.getColumnDataType(), false) +
                " DEFAULT " + typeConverter.getDataType(defaultValue).convertObjectToString(defaultValue, database) +
                ")";

        return new Sql[]{
            new UnparsedSql(sql,
                    new Column()
                            .setTable(new Table(statement.getTableName()).setSchema(statement.getSchemaName()))
                            .setName(statement.getColumnName()))
        };
    }

}
