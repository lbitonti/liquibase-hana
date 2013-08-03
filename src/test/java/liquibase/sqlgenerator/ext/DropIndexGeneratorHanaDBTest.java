package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropColumnStatement;
import liquibase.statement.core.DropIndexStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DropIndexGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<DropIndexStatement> {

    public DropIndexGeneratorHanaDBTest() throws Exception {
        this(new DropIndexGeneratorHanaDB());
    }

    protected DropIndexGeneratorHanaDBTest(SqlGenerator<DropIndexStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected DropIndexStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        DropIndexStatement dropIndexStatement = new DropIndexStatement("index_name", null, "table_name", null);
        return dropIndexStatement;
    }


    @Test
    public void testDropIndexNoSchema() {
        Database database = new HanaDBDatabase();
        DropIndexStatement statement = new DropIndexStatement("index_name", null, "table_name", null);

        assertEquals("DROP INDEX \"index_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testDropIndexWithSchema() {
        Database database = new HanaDBDatabase();
        DropIndexStatement statement = new DropIndexStatement("index_name", "schema_name", "table_name", null);

        assertEquals("DROP INDEX \"schema_name\".\"index_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
