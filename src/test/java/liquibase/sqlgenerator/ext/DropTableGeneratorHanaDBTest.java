package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropSequenceStatement;
import liquibase.statement.core.DropTableStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DropTableGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<DropTableStatement> {

    public DropTableGeneratorHanaDBTest() throws Exception {
        this(new DropTableGeneratorHanaDB());
    }

    protected DropTableGeneratorHanaDBTest(SqlGenerator<DropTableStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected DropTableStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        DropTableStatement dropTableStatement = new DropTableStatement(null, null, "table_name", false);
        return dropTableStatement;
    }


    @Test
    public void testDropTableNoSchema() {
        Database database = new HanaDBDatabase();
        DropTableStatement statement = new DropTableStatement(null, null, "table_name", false);

        assertEquals("DROP TABLE \"table_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testDropTableAndCascadeWithSchema() {
        Database database = new HanaDBDatabase();
        DropTableStatement statement = new DropTableStatement(null, "schema_name", "table_name", true);

        assertEquals("DROP TABLE \"schema_name\".\"table_name\" CASCADE",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
