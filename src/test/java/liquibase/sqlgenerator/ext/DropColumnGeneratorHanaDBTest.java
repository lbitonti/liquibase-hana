package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.CreateViewStatement;
import liquibase.statement.core.DropColumnStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DropColumnGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<DropColumnStatement> {

    public DropColumnGeneratorHanaDBTest() throws Exception {
        this(new DropColumnGeneratorHanaDB());
    }

    protected DropColumnGeneratorHanaDBTest(SqlGenerator<DropColumnStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected DropColumnStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        DropColumnStatement dropColumnStatement = new DropColumnStatement(null, "table_name", "column_name");
        return dropColumnStatement;
    }


    @Test
    public void testSingleColumnDropNoSchema() {
        Database database = new HanaDBDatabase();
        DropColumnStatement statement = new DropColumnStatement(null, "table_name", "column_name");

        assertEquals("ALTER TABLE \"table_name\" DROP (\"column_name\")",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testSingleColumnDropWithSchema() {
        Database database = new HanaDBDatabase();
        DropColumnStatement statement = new DropColumnStatement("schema_name", "table_name", "column_name");

        assertEquals("ALTER TABLE \"schema_name\".\"table_name\" DROP (\"column_name\")",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
