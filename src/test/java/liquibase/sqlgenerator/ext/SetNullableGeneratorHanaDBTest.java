package liquibase.sqlgenerator.ext;

import static org.junit.Assert.assertEquals;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.SetNullableStatement;

import org.junit.Before;
import org.junit.Test;


public class SetNullableGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<SetNullableStatement> {

    private HanaDBDatabase database;
    
    public SetNullableGeneratorHanaDBTest() throws Exception {
        this(new SetNullableGeneratorHanaDB());
    }

    protected SetNullableGeneratorHanaDBTest(SqlGenerator<SetNullableStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

    @Before
    public void setup() throws Exception {
        database = getHanaDBDatabaseMockingEmptyForeignKeyConstraints();
    }

    @Override
	protected SetNullableStatement createSampleSqlStatement() {
        SetNullableStatement setNullableStatement = new SetNullableStatement(null, null, "table_name", "column_name", "int unsigned", false);
        return setNullableStatement;
    }


    @Test
    public void testSetNullableNoSchema() {
        SetNullableStatement statement = new SetNullableStatement(null, null, "table_name", "column_name", "int", false);

        assertEquals("ALTER TABLE \"table_name\" ALTER (\"column_name\" INTEGER NOT NULL)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testSetNullableWithSchema() {
        SetNullableStatement statement = new SetNullableStatement(null, "schema_name", "table_name", "column_name", "int", true);

        assertEquals("ALTER TABLE \"schema_name\".\"table_name\" ALTER (\"column_name\" INTEGER NULL)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
