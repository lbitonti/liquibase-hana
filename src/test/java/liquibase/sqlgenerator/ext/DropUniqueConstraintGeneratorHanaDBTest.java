package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.DropPrimaryKeyStatement;
import liquibase.statement.core.DropUniqueConstraintStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DropUniqueConstraintGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<DropUniqueConstraintStatement> {

    public DropUniqueConstraintGeneratorHanaDBTest() throws Exception {
        this(new DropUniqueConstraintGeneratorHanaDB());
    }

    protected DropUniqueConstraintGeneratorHanaDBTest(SqlGenerator<DropUniqueConstraintStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected DropUniqueConstraintStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        DropUniqueConstraintStatement dropUniqueConstraintStatement = new DropUniqueConstraintStatement(null, "table_name", "constraint_name");
        return dropUniqueConstraintStatement;
    }


    @Test
    public void testDropUniqueConstraintNoSchema() {
        Database database = new HanaDBDatabase();
        DropUniqueConstraintStatement statement =
                new DropUniqueConstraintStatement(null, "table_name", "constraint_name");

        assertEquals("ALTER TABLE \"table_name\" DROP CONSTRAINT \"constraint_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testDropUniqueConstraintWithSchema() {
        Database database = new HanaDBDatabase();
        DropUniqueConstraintStatement statement =
                new DropUniqueConstraintStatement("schema_name", "table_name", "constraint_name");

        assertEquals("ALTER TABLE \"schema_name\".\"table_name\" DROP CONSTRAINT \"constraint_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
