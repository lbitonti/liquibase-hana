package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropIndexStatement;
import liquibase.statement.core.DropViewStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DropViewGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<DropViewStatement> {

    public DropViewGeneratorHanaDBTest() throws Exception {
        this(new DropViewGeneratorHanaDB());
    }

    protected DropViewGeneratorHanaDBTest(SqlGenerator<DropViewStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected DropViewStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        DropViewStatement dropViewStatement = new DropViewStatement(null, "view_name");
        return dropViewStatement;
    }


    @Test
    public void testDropViewNoSchema() {
        Database database = new HanaDBDatabase();
        DropViewStatement statement = new DropViewStatement(null, "view_name");

        assertEquals("DROP VIEW \"view_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testDropViewWithSchema() {
        Database database = new HanaDBDatabase();
        DropViewStatement statement = new DropViewStatement("schema_name", "view_name");

        assertEquals("DROP VIEW \"schema_name\".\"view_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
