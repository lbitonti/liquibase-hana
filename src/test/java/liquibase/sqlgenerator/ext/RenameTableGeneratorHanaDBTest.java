package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.statement.core.RenameColumnStatement;
import liquibase.statement.core.RenameTableStatement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class RenameTableGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<RenameTableStatement> {

    public RenameTableGeneratorHanaDBTest() throws Exception {
        this(new RenameTableGeneratorHanaDB());
    }

    protected RenameTableGeneratorHanaDBTest(SqlGenerator<RenameTableStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected RenameTableStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        RenameTableStatement renameTableStatement = new RenameTableStatement(null, null, "old_table_name", "new_table_name");
        return renameTableStatement;
    }


    @Test
    public void testRenameTableNoSchema() {
        Database database = new HanaDBDatabase();
        RenameTableStatement statement = new RenameTableStatement(null, null, "old_table_name", "new_table_name");

        assertEquals("RENAME TABLE \"old_table_name\" TO \"new_table_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testRenameTableWithSchema() {
        Database database = new HanaDBDatabase();
        RenameTableStatement statement = new RenameTableStatement(null, "schema_name", "old_table_name", "new_table_name");

        assertEquals("RENAME TABLE \"schema_name\".\"old_table_name\" TO \"new_table_name\"",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
