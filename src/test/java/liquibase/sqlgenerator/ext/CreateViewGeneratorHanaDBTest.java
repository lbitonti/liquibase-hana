package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.CreateViewStatement;
import org.junit.Test;

import static org.junit.Assert.*;


public class CreateViewGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<CreateViewStatement> {

    public CreateViewGeneratorHanaDBTest() throws Exception {
        this(new CreateViewGeneratorHanaDB());
    }

    protected CreateViewGeneratorHanaDBTest(SqlGenerator<CreateViewStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected CreateViewStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        CreateViewStatement createViewStatement = new CreateViewStatement(null, "view_name", "SELECT * FROM A", false);
        return createViewStatement;
    }


    @Test
    public void testWithBasicSelect() {
        Database database = new HanaDBDatabase();
        CreateViewStatement statement = new CreateViewStatement(null, "view_name", "SELECT * FROM A", false);

        assertEquals("CREATE VIEW \"view_name\" AS SELECT * FROM A",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
