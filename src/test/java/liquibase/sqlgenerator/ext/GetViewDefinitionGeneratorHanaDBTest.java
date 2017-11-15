package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropViewStatement;
import liquibase.statement.core.GetViewDefinitionStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class GetViewDefinitionGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<GetViewDefinitionStatement> {

    public GetViewDefinitionGeneratorHanaDBTest() throws Exception {
        this(new GetViewDefinitionGeneratorHanaDB());
    }

    protected GetViewDefinitionGeneratorHanaDBTest(SqlGenerator<GetViewDefinitionStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected GetViewDefinitionStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        GetViewDefinitionStatement getViewDefinitionStatement = new GetViewDefinitionStatement(null, null, "view_name");
        return getViewDefinitionStatement;
    }


    @Test
    public void testGetViewNoSchema() {
        Database database = new HanaDBDatabase();
        GetViewDefinitionStatement statement = new GetViewDefinitionStatement(null, null, "actual_view_name");

        assertEquals("SELECT DEFINITION FROM VIEWS WHERE upper(VIEW_NAME)='ACTUAL_VIEW_NAME'",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testGetViewWithSchema() {
        Database database = new HanaDBDatabase();
        GetViewDefinitionStatement statement = new GetViewDefinitionStatement(null, "schema_name", "actual_view_name");
        
        assertEquals("SELECT DEFINITION FROM VIEWS WHERE upper(SCHEMA_NAME)='SCHEMA_NAME' AND upper(VIEW_NAME)='ACTUAL_VIEW_NAME'",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
