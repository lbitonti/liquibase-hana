package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.MockSqlGeneratorChain;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.core.AddDefaultValueStatement;
import org.junit.Test;

import static org.junit.Assert.*;


public class AddDefaultValueGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<AddDefaultValueStatement> {

    public AddDefaultValueGeneratorHanaDBTest() throws Exception {
        this(new AddDefaultValueGeneratorHanaDB());
    }

    protected AddDefaultValueGeneratorHanaDBTest(SqlGenerator<AddDefaultValueStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected AddDefaultValueStatement createSampleSqlStatement() {
		return new AddDefaultValueStatement(null, null, "table_name", "column_name", "column_type", "default_value");
	}


    @Test
    public void testAddIntegerDefaultValue() throws Exception {
        super.isValid();
        AddDefaultValueStatement addDefaultValueStatement =
                new AddDefaultValueStatement(null, null, "table_name", "column_name", "java.sql.Types.INTEGER", new Integer(12));

        Database hanadb = new HanaDBDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertFalse(generatorUnderTest.validate(addDefaultValueStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
        Sql[] generatedSql = generatorUnderTest.generateSql(addDefaultValueStatement, hanadb, sqlGeneratorChain);
        assertTrue(generatedSql.length == 1);
        assertEquals("ALTER TABLE \"table_name\" ALTER (\"column_name\" INTEGER DEFAULT 12)", generatedSql[0].toSql());

    }

    @Test
    public void testAddVarcharDefaultValue() throws Exception {
        super.isValid();
        AddDefaultValueStatement addDefaultValueStatement =
                new AddDefaultValueStatement(null, null, "table_name", "column_name", "VARCHAR", "default_value");

        Database hanadb = new HanaDBDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertFalse(generatorUnderTest.validate(addDefaultValueStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
        Sql[] generatedSql = generatorUnderTest.generateSql(addDefaultValueStatement, hanadb, sqlGeneratorChain);
        assertTrue(generatedSql.length == 1);
        assertEquals("ALTER TABLE \"table_name\" ALTER (\"column_name\" VARCHAR DEFAULT 'default_value')", generatedSql[0].toSql());

    }

}
