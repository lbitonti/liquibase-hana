package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropIndexStatement;
import liquibase.statement.core.DropPrimaryKeyStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DropPrimaryKeyGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<DropPrimaryKeyStatement> {

    public DropPrimaryKeyGeneratorHanaDBTest() throws Exception {
        this(new DropPrimaryKeyGeneratorHanaDB());
    }

    protected DropPrimaryKeyGeneratorHanaDBTest(SqlGenerator<DropPrimaryKeyStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected DropPrimaryKeyStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        DropPrimaryKeyStatement dropPrimaryKeyStatement = new DropPrimaryKeyStatement(null, null, "table_name", null);
        return dropPrimaryKeyStatement;
    }


    @Test
    public void testDropPrimaryKeyNoSchema() {
        Database database = new HanaDBDatabase();
        DropPrimaryKeyStatement statement = new DropPrimaryKeyStatement(null, null, "table_name", null);

        assertEquals("ALTER TABLE \"table_name\" DROP PRIMARY KEY",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testDropPrimaryKeyWithSchema() {
        Database database = new HanaDBDatabase();
        DropPrimaryKeyStatement statement = new DropPrimaryKeyStatement(null, "schema_name", "table_name", null);

        assertEquals("ALTER TABLE \"schema_name\".\"table_name\" DROP PRIMARY KEY",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
