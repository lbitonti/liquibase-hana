package liquibase.sqlgenerator.ext;

import liquibase.change.AddColumnConfig;
import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.MockSqlGeneratorChain;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.core.AlterSequenceStatement;
import liquibase.statement.core.CreateIndexStatement;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;


public class CreateIndexGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<CreateIndexStatement> {

    public CreateIndexGeneratorHanaDBTest() throws Exception {
        this(new CreateIndexGeneratorHanaDB());
    }

    protected CreateIndexGeneratorHanaDBTest(SqlGenerator<CreateIndexStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected CreateIndexStatement createSampleSqlStatement() {
        CreateIndexStatement createIndexStatement =
                new CreateIndexStatement("index_name", null, null, "table_name", true, null, (AddColumnConfig) new AddColumnConfig().setName("column_name"));
        return createIndexStatement;
    }


    @Test
    public void testCreateSingleColumnUniqueIndex() throws Exception {
        super.isValid();
        CreateIndexStatement createIndexStatement =
                new CreateIndexStatement("index_name", null, null, "table_name", true, null, (AddColumnConfig) new AddColumnConfig().setName("column_name"));

        Database hanadb = new HanaDBDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertFalse(generatorUnderTest.validate(createIndexStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
        Sql[] generatedSql = generatorUnderTest.generateSql(createIndexStatement, hanadb, sqlGeneratorChain);
        assertTrue(generatedSql.length == 1);
        assertEquals("CREATE UNIQUE INDEX \"index_name\" ON \"table_name\" (\"column_name\")", generatedSql[0].toSql());

    }

    @Test
    public void testCreateMultiColumnNonUniqueIndex() throws Exception {
        super.isValid();
        CreateIndexStatement createIndexStatement =
                new CreateIndexStatement("index_name", null, null, "table_name", null, null, (AddColumnConfig) new AddColumnConfig().setName("column_name_1"), (AddColumnConfig) new AddColumnConfig().setName("column_name_2"));

        Database hanadb = new HanaDBDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertFalse(generatorUnderTest.validate(createIndexStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
        Sql[] generatedSql = generatorUnderTest.generateSql(createIndexStatement, hanadb, sqlGeneratorChain);
        assertTrue(generatedSql.length == 1);
        assertEquals("CREATE INDEX \"index_name\" ON \"table_name\" (\"column_name_1\", \"column_name_2\")", generatedSql[0].toSql());

    }

}
