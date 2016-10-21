package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.datatype.DatabaseDataType;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.ModifyDataTypeGenerator;
import liquibase.statement.core.InsertOrUpdateStatement;
import liquibase.statement.core.ModifyDataTypeStatement;

/**
 * Created by Benjamin on 21. okt. 2016.
 */
public class ModifyDataTypeGeneratorHanaDB extends ModifyDataTypeGenerator {

    @Override
    public boolean supports(ModifyDataTypeStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public Sql[] generateSql(ModifyDataTypeStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        if (!supports(statement, database)) {
            return sqlGeneratorChain.generateSql(statement, database);
        }

        StringBuffer buffer = new StringBuffer();
        buffer
                .append("ALTER TABLE ")
                .append(database.escapeSequenceName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName()))
                .append(" ALTER (")
                .append(database.escapeColumnName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName(), statement.getColumnName()))
                .append(" ");

        DatabaseDataType newDataType = DataTypeFactory.getInstance().fromDescription(statement.getNewDataType(), database).toDatabaseDataType(database);
        buffer
                .append(newDataType)
                .append(")");

        String sql = buffer.toString();

        return new Sql[]{new UnparsedSql(sql, getAffectedTable(statement))};
    }
}
