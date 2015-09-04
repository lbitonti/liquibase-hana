package liquibase.database.core;

import liquibase.CatalogAndSchema;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.RanChangeSet;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.exception.DatabaseException;
import liquibase.exception.DatabaseHistoryException;
import liquibase.exception.DateParseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.RollbackImpossibleException;
import liquibase.exception.StatementNotSupportedOnDatabaseException;
import liquibase.structure.DatabaseObject;
import liquibase.sql.visitor.SqlVisitor;
import liquibase.statement.DatabaseFunction;
import liquibase.statement.SqlStatement;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MockDatabase implements Database {

    @Override
    public int getPriority() {
        return PRIORITY_DEFAULT;
    }

    public String getName() {
        return "Mock Database";
    }

    @Override
    public CatalogAndSchema getDefaultSchema() {
        return new CatalogAndSchema("default", "default");
    }

    @Override
    public Integer getDefaultPort() {
        return null;
    }

    @Override
    public void setCanCacheLiquibaseTableInfo(boolean canCacheLiquibaseTableInfo) {
        //
    }

    @Override
    public boolean requiresUsername() {
        return false;
    }

    @Override
    public boolean requiresPassword() {
        return false;
    }

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return false;
    }

    @Override
    public String getDefaultDriver(String url) {
        return null;
    }

    @Override
    public DatabaseConnection getConnection() {
        return null;
    }

    @Override
    public void setConnection(DatabaseConnection conn) {
    }

    @Override
    public boolean getAutoCommitMode() {
        return false;
    }

    @Override
    public boolean isAutoCommit() throws DatabaseException {
        return false;
    }


    @Override
    public boolean isCaseSensitive() {
        return false;
    }

    @Override
    public void setAutoCommit(boolean b) throws DatabaseException {

    }

    @Override
    public boolean supportsDDLInTransaction() {
        return false;
    }

    @Override
    public String getDatabaseProductName() {
        return null;
    }

    @Override
    public String getDatabaseProductVersion() throws DatabaseException {
        return null;
    }


    @Override
    public int getDatabaseMajorVersion() throws DatabaseException {
        return 0;
    }

    @Override
    public int getDatabaseMinorVersion() throws DatabaseException {
        return 0;
    }

    @Override
    public String getShortName() {
        return null;
    }

    @Override
    public String getDefaultCatalogName() {
        return null;
    }

    @Override
    public void setDefaultCatalogName(String catalogName) throws DatabaseException {

    }

    @Override
    public String getDefaultSchemaName()  {
        return null;
    }

    @Override
    public void setDefaultSchemaName(String schemaName) throws DatabaseException {

    }

    @Override
    public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    @Override
    public boolean supportsSequences() {
        return true;
    }

    @Override
    public boolean supportsDropTableCascadeConstraints() {
        return false;
    }

    @Override
    public boolean supportsAutoIncrement() {
        return true;
    }

    @Override
    public String getDateLiteral(String isoDate) {
        return isoDate;
    }


    @Override
    public String getDateLiteral(java.sql.Date date) {
        return date.toString();
    }

    @Override
    public String getTimeLiteral(Time time) {
        return time.toString();
    }

    @Override
    public String getDateTimeLiteral(Timestamp timeStamp) {
        return timeStamp.toString();
    }

    @Override
    public String getCurrentDateTimeFunction() {
        return "DATETIME()";
    }

    @Override
    public void setCurrentDateTimeFunction(String function) {
    }

    @Override
    public String getLineComment() {
        return null;
    }

    @Override
    public String getAutoIncrementClause(BigInteger startWith, BigInteger incrementBy) {
        return "AUTO_INCREMENT_CLAUSE"
                + startWith != null ? (" " + startWith) : ""
                + incrementBy != null ? (" " + incrementBy) : "";
    }

    /**
     * @see liquibase.database.Database#getDatabaseChangeLogTableName()
     */
    @Override
    public String getDatabaseChangeLogTableName() {
        return "DATABASECHANGELOG";
    }

    /**
     * @see liquibase.database.Database#getDatabaseChangeLogLockTableName()
     */
    @Override
    public String getDatabaseChangeLogLockTableName() {
        return "DATABASECHANGELOGLOCK";
    }

    /**
     * Does nothing
     *
     * @see liquibase.database.Database#setDatabaseChangeLogLockTableName(java.lang.String)
     */
    @Override
    public void setDatabaseChangeLogLockTableName(String tableName) {
    }

    /**
     * Does nothing
     *
     * @see liquibase.database.Database#setDatabaseChangeLogTableName(java.lang.String)
     */
    @Override
    public void setDatabaseChangeLogTableName(String tableName) {
    }

    @Override
    public String getConcatSql(String... values) {
        return null;
    }

    @Override
    public void dropDatabaseObjects(CatalogAndSchema schema) throws DatabaseException {
    }

    @Override
    public void tag(String tagString) throws DatabaseException {
    }

    @Override
    public boolean doesTagExist(String tag) throws DatabaseException {
        return false;
    }


    @Override
    public boolean isSystemObject(DatabaseObject example) {
        return false;
    }

    @Override
    public boolean isLiquibaseObject(DatabaseObject object) {
        return false;
    }

    @Override
    public boolean supportsTablespaces() {
        return false;
    }

    @Override
    public String getViewDefinition(CatalogAndSchema schema, String name) throws DatabaseException {
        return null;
    }

    @Override
    public String getDateLiteral(Date defaultDateValue) {
        return defaultDateValue.toString();
    }

    @Override
    public String escapeTableName(String catalogName, String schemaName, String tableName) {
        if (schemaName == null) {
            return tableName;
        } else {
            return schemaName+"."+tableName;
        }
    }

    @Override
    public String escapeIndexName(String catalogName, String schemaName, String indexName) {
        return escapeTableName(catalogName, schemaName, indexName);
    }

    @Override
    public String escapeColumnName(String catalogName, String schemaName, String tableName, String columnName) {
        return columnName;
    }

    @Override
    public String escapeColumnName(String catalogName, String schemaName, String tableName, String columnName, boolean quoteNamesThatMayBeFunctions) {
        return columnName;
    }

    @Override
    public String escapeColumnNameList(String columnNames) {
        return columnNames;
    }

    @Override
    public String escapeSequenceName(String catalogName, String schemaName, String sequenceName) {
        if (sequenceName == null) {
            return sequenceName;
        } else {
            return schemaName+"."+sequenceName;
        }
    }

    @Override
    public boolean supportsSchemas() {
        return true;
    }

    @Override
    public boolean supportsCatalogs() {
        return true;
    }

    @Override
    public String generatePrimaryKeyName(String tableName) {
        return "PK_"+tableName;
    }

    @Override
    public String escapeViewName(String catalogName, String schemaName, String viewName) {
        return escapeTableName(catalogName, schemaName, viewName);
    }

    @Override
    public ChangeSet.RunStatus getRunStatus(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return null;
    }

    @Override
    public RanChangeSet getRanChangeSet(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return null;
    }

    @Override
    public void markChangeSetExecStatus(ChangeSet changeSet, ChangeSet.ExecType execType) throws DatabaseException {
        ;
    }

    @Override
    public List<RanChangeSet> getRanChangeSetList() throws DatabaseException {
        return null;
    }

    @Override
    public Date getRanDate(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return null;
    }

    @Override
    public void removeRanStatus(ChangeSet changeSet) throws DatabaseException {
        ;
    }

    @Override
    public void commit() {
        ;
    }

    @Override
    public void rollback() {
        ;
    }

    @Override
    public String escapeStringForDatabase(String string) {
        return string;
    }

    @Override
    public void close() throws DatabaseException {
        ;
    }

    @Override
    public boolean supportsRestrictForeignKeys() {
        return true;
    }

    @Override
    public String escapeConstraintName(String constraintName) {
        return constraintName;
    }

    @Override
    public boolean isSafeToRunUpdate() throws DatabaseException {
        return true;
    }

    @Override
    public String escapeObjectName(String objectName, Class<? extends DatabaseObject> objectType) {
        return objectName;
    }

    @Override
    public String escapeObjectName(String catalogName, String schemaName, String objectName, Class<? extends DatabaseObject> objectType) {
        return catalogName +"."+schemaName+"."+objectName;
    }

    @Override
    public void executeStatements(Change change, DatabaseChangeLog changeLog, List<SqlVisitor> sqlVisitors) throws LiquibaseException {
        ;
    }

    @Override
    public void execute(SqlStatement[] statements, List<SqlVisitor> sqlVisitors) throws LiquibaseException {
        ;
    }

    @Override
    public void saveStatements(Change change, List<SqlVisitor> sqlVisitors, Writer writer) throws IOException, StatementNotSupportedOnDatabaseException, LiquibaseException {
        ;
    }

    @Override
    public void executeRollbackStatements(Change change, List<SqlVisitor> sqlVisitors) throws LiquibaseException, RollbackImpossibleException {
        ;
    }

    @Override
    public void executeRollbackStatements(SqlStatement[] statements, List<SqlVisitor> sqlVisitors) throws LiquibaseException, RollbackImpossibleException {
        ;
    }

    @Override
    public void saveRollbackStatement(Change change, List<SqlVisitor> sqlVisitors, Writer writer) throws IOException, RollbackImpossibleException, StatementNotSupportedOnDatabaseException, LiquibaseException {
        ;
    }

    @Override
    public String getLiquibaseCatalogName() {
        return null;
    }

	@Override
    public void setLiquibaseCatalogName(String catalogName) {
	}

    @Override
    public String getLiquibaseSchemaName(){
        return null;
    }

	@Override
    public void setLiquibaseSchemaName(String schemaName) {
	}

    @Override
    public String getLiquibaseTablespaceName() {
        return null;
    }

    @Override
    public void setLiquibaseTablespaceName(String tablespaceName) {

    }

    @Override
    public Date parseDate(String dateAsString) throws DateParseException {
        return new Date();
    }

    @Override
    public List<DatabaseFunction> getDateFunctions() {
        return null;
    }

    @Override
    public void resetInternalState() {

    }

    @Override
    public boolean supportsForeignKeyDisable() {
        return false;
    }

    @Override
    public boolean disableForeignKeyChecks() throws DatabaseException {
        return false;
    }

    @Override
    public void enableForeignKeyChecks() throws DatabaseException {

    }

    @Override
    public boolean isReservedWord(String string) {
        return false;
    }

    @Override
    public CatalogAndSchema correctSchema(CatalogAndSchema schema) {
        return schema;
    }

    @Override
    public String correctObjectName(String name, Class<? extends DatabaseObject> objectType) {
        return name;
    }

    @Override
    public boolean isFunction(String string) {
        if (string.endsWith("()")) {
            return true;
        }
        return false;
    }

    @Override
    public int getDataTypeMaxParameters(String dataTypeName) {
        return 2;
    }

    @Override
    public boolean dataTypeIsNotModifiable(String typeName) {
        return true;
    }

    @Override
    public String generateDatabaseFunctionValue(final DatabaseFunction databaseFunction) {
        return null;
    }

    @Override
    public void setObjectQuotingStrategy(ObjectQuotingStrategy quotingStrategy) {
    }

    @Override
    public ObjectQuotingStrategy getObjectQuotingStrategy() {
        return ObjectQuotingStrategy.LEGACY;
    }

    @Override
    public boolean supportsCatalogInObjectName(Class<? extends DatabaseObject> type) {
        return true;
    }

	@Override
    public boolean createsIndexesForForeignKeys() {
		return false;
	}

	@Override
    public void setOutputDefaultSchema(boolean outputDefaultSchema) {
	}

	@Override
    public boolean getOutputDefaultSchema() {
		return false;
	}

	@Override
    public boolean isDefaultSchema(String catalog, String schema) {
		return false;
	}

	@Override
    public boolean isDefaultCatalog(String catalog) {
		return false;
	}

	@Override
    public boolean getOutputDefaultCatalog() {
		return false;
	}

	@Override
    public void setOutputDefaultCatalog(boolean outputDefaultCatalog) {
	}

	@Override
    public boolean supportsPrimaryKeyNames() {
		return true;
	}

	@Override
    public String getSystemSchema() {
		return "SYSTEM";
	}

	@Override
    public void addReservedWords(Collection<String> words) {
	}

    @Override
    public String escapeDataTypeName(String dataTypeName) {
        return dataTypeName;
    }

    @Override
    public String unescapeDataTypeName(String dataTypeName) {
        return dataTypeName;
    }

    @Override
    public String unescapeDataTypeString(String dataTypeString) {
        return dataTypeString;
    }
}
