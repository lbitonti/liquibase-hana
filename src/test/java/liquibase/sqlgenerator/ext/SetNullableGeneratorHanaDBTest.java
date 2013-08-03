package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropUniqueConstraintStatement;
import liquibase.statement.core.SetNullableStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class SetNullableGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<SetNullableStatement> {

    public SetNullableGeneratorHanaDBTest() throws Exception {
        this(new SetNullableGeneratorHanaDB());
    }

    protected SetNullableGeneratorHanaDBTest(SqlGenerator<SetNullableStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected SetNullableStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        SetNullableStatement setNullableStatement = new SetNullableStatement(null, "table_name", "column_name", "int unsigned", false);
        return setNullableStatement;
    }


    @Test
    public void testSetNullableNoSchema() {
        Database database = new HanaDBDatabase();
        SetNullableStatement statement = new SetNullableStatement(null, "table_name", "column_name", "int", false);

        assertEquals("ALTER TABLE \"table_name\" ALTER (\"column_name\" INTEGER NOT NULL)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testSetNullableWithSchema() {
        Database database = new HanaDBDatabase();
        SetNullableStatement statement = new SetNullableStatement("schema_name", "table_name", "column_name", "int", true);

        assertEquals("ALTER TABLE \"schema_name\".\"table_name\" ALTER (\"column_name\" INTEGER NULL)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
