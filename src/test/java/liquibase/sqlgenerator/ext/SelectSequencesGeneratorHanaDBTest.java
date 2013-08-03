package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.GetViewDefinitionStatement;
import liquibase.statement.core.SelectSequencesStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class SelectSequencesGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<SelectSequencesStatement> {

    public SelectSequencesGeneratorHanaDBTest() throws Exception {
        this(new SelectSequencesGeneratorHanaDB());
    }

    protected SelectSequencesGeneratorHanaDBTest(SqlGenerator<SelectSequencesStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected SelectSequencesStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        SelectSequencesStatement selectSequencesStatement = new SelectSequencesStatement("schema_name");
        return selectSequencesStatement;
    }


    @Test
    public void testSelectSequencesNoSchema() {
        Database database = new HanaDBDatabase();
        SelectSequencesStatement statement = new SelectSequencesStatement("schema_name");

        assertEquals("SELECT SEQUENCE_NAME FROM SEQUENCES WHERE upper(SCHEMA_NAME) = 'SCHEMA_NAME'",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
