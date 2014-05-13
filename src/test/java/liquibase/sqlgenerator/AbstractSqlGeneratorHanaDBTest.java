package liquibase.sqlgenerator;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.CreateTableStatement;
import org.junit.Test;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

public abstract class AbstractSqlGeneratorHanaDBTest<T extends SqlStatement> {

    protected SqlGenerator<T> generatorUnderTest;

    public AbstractSqlGeneratorHanaDBTest(SqlGenerator<T> generatorUnderTest) throws Exception {
        this.generatorUnderTest = generatorUnderTest;
    }

    protected abstract T createSampleSqlStatement();

    protected void dropAndCreateTable(CreateTableStatement statement, Database database) throws SQLException, DatabaseException {
        ExecutorService.getInstance().getExecutor(database).execute(statement);

        if (!database.getAutoCommitMode()) {
            database.getConnection().commit();
        }

    }

    @Test
    public void isImplementation() throws Exception {
//        for (Database database : TestContextHanaDB.getInstance().getAllDatabases()) {
//            boolean isImpl = generatorUnderTest.supports(createSampleSqlStatement(), database);
//            if (shouldBeImplementation(database)) {
//                assertTrue("Unexpected false supports for " + database.getTypeName(), isImpl);
//            } else {
//                assertFalse("Unexpected true supports for " + database.getTypeName(), isImpl);
//            }
//        }
        Database database = new HanaDBDatabase();
        boolean isImpl = generatorUnderTest.supports(createSampleSqlStatement(), database);
        if (shouldBeImplementation(database)) {
            assertTrue("Unexpected false supports for " + database.getShortName(), isImpl);
        } else {
            assertFalse("Unexpected true supports for " + database.getShortName(), isImpl);
        }
    }

    @Test
    public void isValid() throws Exception {
//        for (Database database : TestContextHanaDB.getInstance().getAllDatabases()) {
//        	if (shouldBeImplementation(database)) {
//            	if (waitForException(database)) {
//            		assertTrue("The validation should be failed for " + database, generatorUnderTest.validate(createSampleSqlStatement(), database, new MockSqlGeneratorChain()).hasErrors());
//            	} else {
//            		assertFalse("isValid failed against " + database, generatorUnderTest.validate(createSampleSqlStatement(), database, new MockSqlGeneratorChain()).hasErrors());
//            	}
//
//        	}
//        }
        Database database = new HanaDBDatabase();
        if (shouldBeImplementation(database)) {
            if (waitForException(database)) {
                assertTrue("The validation should be failed for " + database, generatorUnderTest.validate(createSampleSqlStatement(), database, new MockSqlGeneratorChain()).hasErrors());
            } else {
                assertFalse("isValid failed against " + database, generatorUnderTest.validate(createSampleSqlStatement(), database, new MockSqlGeneratorChain()).hasErrors());
            }
        }
    }

    @Test
    public void checkExpectedGenerator() {
        assertEquals(this.getClass().getName().replaceFirst("Test$", ""), generatorUnderTest.getClass().getName());
    }

    protected boolean waitForException(Database database) {
        return false;
    }

    protected boolean shouldBeImplementation(Database database) {
        return true;
    }

    protected HanaDBDatabase getHanaDBDatabaseMockingEmptyForeignKeyConstraints() throws SQLException, DatabaseException {
            ResultSet empty = createNiceMock(ResultSet.class);
            expect(empty.next()).andReturn(false).once();
            replay(empty);
            
            DatabaseMetaData md = createNiceMock(DatabaseMetaData.class);
            expect(md.getImportedKeys(anyString(), anyString(), anyString())).andReturn(empty);
            expect(md.getExportedKeys(anyString(), anyString(), anyString())).andReturn(empty);
            replay(md);

            final JdbcConnection mockConnection = createNiceMock(JdbcConnection.class);
            expect(mockConnection.getMetaData()).andReturn(md).anyTimes();
            replay(mockConnection);
            
            return new HanaDBDatabase() {
                public liquibase.database.DatabaseConnection getConnection() {
                    return mockConnection;
                }
            };
    }
}
