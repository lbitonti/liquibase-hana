package liquibase.sqlgenerator.ext;

import static org.junit.Assert.assertEquals;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropColumnStatement;

import org.junit.Before;
import org.junit.Test;


public class DropColumnGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<DropColumnStatement> {

    private HanaDBDatabase database;
    
    public DropColumnGeneratorHanaDBTest() throws Exception {
        this(new DropColumnGeneratorHanaDB());
    }

    protected DropColumnGeneratorHanaDBTest(SqlGenerator<DropColumnStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

    @Before
    public void setup() throws Exception {
        database = getHanaDBDatabaseMockingEmptyForeignKeyConstraints();
    }

    @Override
	protected DropColumnStatement createSampleSqlStatement() {
        DropColumnStatement dropColumnStatement = new DropColumnStatement(null, null, "table_name", "column_name");
        return dropColumnStatement;
    }


    @Test
    public void testSingleColumnDropNoSchema() {
        DropColumnStatement statement = new DropColumnStatement(null, null, "table_name", "column_name");

        assertEquals("ALTER TABLE \"table_name\" DROP (\"column_name\")",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testSingleColumnDropWithSchema() {
        DropColumnStatement statement = new DropColumnStatement(null, "schema_name", "table_name", "column_name");

        assertEquals("ALTER TABLE \"schema_name\".\"table_name\" DROP (\"column_name\")",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
