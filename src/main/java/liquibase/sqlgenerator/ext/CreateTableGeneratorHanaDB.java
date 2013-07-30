package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.datatype.DatabaseDataType;
import liquibase.logging.LogFactory;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.CreateTableGenerator;
import liquibase.statement.AutoIncrementConstraint;
import liquibase.statement.UniqueConstraint;
import liquibase.statement.core.CreateTableStatement;
import liquibase.util.StringUtils;

import java.util.Iterator;


public class CreateTableGeneratorHanaDB extends CreateTableGenerator {

    @Override
    public Sql[] generateSql(CreateTableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE ").append(database.escapeTableName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName())).append(" ");
        buffer.append("(");

        boolean isSinglePrimaryKeyColumn = statement.getPrimaryKeyConstraint() != null
                && statement.getPrimaryKeyConstraint().getColumns().size() == 1;

        boolean isPrimaryKeyAutoIncrement = false;

        Iterator<String> columnIterator = statement.getColumns().iterator();
        while (columnIterator.hasNext()) {
            String column = columnIterator.next();

            DatabaseDataType columnType = statement.getColumnTypes().get(column).toDatabaseDataType(database);
            buffer.append(database.escapeColumnName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName(), column));

            buffer.append(" ").append(columnType);

            AutoIncrementConstraint autoIncrementConstraint = null;

            for (AutoIncrementConstraint currentAutoIncrementConstraint : statement.getAutoIncrementConstraints()) {
                if (column.equals(currentAutoIncrementConstraint.getColumnName())) {
                    autoIncrementConstraint = currentAutoIncrementConstraint;
                    break;
                }
            }

            boolean isAutoIncrementColumn = autoIncrementConstraint != null;
            boolean isPrimaryKeyColumn = statement.getPrimaryKeyConstraint() != null
                    && statement.getPrimaryKeyConstraint().getColumns().contains(column);
            isPrimaryKeyAutoIncrement = isPrimaryKeyAutoIncrement
                    || isPrimaryKeyColumn && isAutoIncrementColumn;

            if (statement.getDefaultValue(column) != null) {
                Object defaultValue = statement.getDefaultValue(column);
                buffer.append(" DEFAULT ");
                buffer.append(statement.getColumnTypes().get(column).objectToSql(defaultValue, database));
            }

            if (isAutoIncrementColumn) {
                LogFactory.getLogger().warning(database.getShortName()+" does not support autoincrement columns as request for "+(database.escapeTableName(statement.getCatalogName(), statement.getSchemaName(), statement.getTableName())));
            }

            if (statement.getNotNullColumns().contains(column)) {
                buffer.append(" NOT NULL");
            }

            if (columnIterator.hasNext()) {
                buffer.append(", ");
            }
        }

        buffer.append(",");

        if (statement.getPrimaryKeyConstraint() != null && statement.getPrimaryKeyConstraint().getColumns().size() > 0) {
            buffer.append(" PRIMARY KEY (");
            buffer.append(database.escapeColumnNameList(StringUtils.join(statement.getPrimaryKeyConstraint().getColumns(), ", ")));
            buffer.append(")");
            buffer.append(",");
        }

        for (UniqueConstraint uniqueConstraint : statement.getUniqueConstraints()) {
            buffer.append(" CONSTRAINT ");
            buffer.append(database.escapeConstraintName(uniqueConstraint.getConstraintName()));
            buffer.append(" UNIQUE (");
            buffer.append(database.escapeColumnNameList(StringUtils.join(uniqueConstraint.getColumns(), ", ")));
            buffer.append(")");
            buffer.append(",");
        }

        String sql = buffer.toString().replaceFirst(",\\s*$", "") + ")";

        if (statement.getTablespace() != null && database.supportsTablespaces()) {
            sql += " TABLESPACE " + statement.getTablespace();
        }

        return new Sql[] {
                new UnparsedSql(sql, getAffectedTable(statement))
        };
    }


    /**
     * @see liquibase.sqlgenerator.core.AbstractSqlGenerator#getPriority()
     */
    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    /**
     * @see liquibase.sqlgenerator.core.AbstractSqlGenerator#supports(liquibase.statement.SqlStatement, liquibase.database.Database)
     */
    @Override
    public boolean supports(CreateTableStatement statement, Database database) {
        return database instanceof HanaDBDatabase;
    }

}
