package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropIndexStatement;
import liquibase.statement.core.DropSequenceStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DropSequenceGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<DropSequenceStatement> {

    public DropSequenceGeneratorHanaDBTest() throws Exception {
        this(new DropSequenceGeneratorHanaDB());
    }

    protected DropSequenceGeneratorHanaDBTest(SqlGenerator<DropSequenceStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected DropSequenceStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        DropSequenceStatement dropSequenceStatement = new DropSequenceStatement(null, "sequence_name");
        return dropSequenceStatement;
    }


    @Test
    public void testDropSequenceNoSchema() {
        Database database = new HanaDBDatabase();
        DropSequenceStatement statement = new DropSequenceStatement(null, "sequence_name");

        assertEquals("DROP SEQUENCE \"sequence_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testDropSequenceWithSchema() {
        Database database = new HanaDBDatabase();
        DropSequenceStatement statement = new DropSequenceStatement("schema_name", "sequence_name");

        assertEquals("DROP SEQUENCE \"schema_name\".\"sequence_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
