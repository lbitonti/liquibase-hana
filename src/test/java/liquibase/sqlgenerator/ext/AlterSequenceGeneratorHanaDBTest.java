package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.MockSqlGeneratorChain;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.core.AddDefaultValueStatement;
import liquibase.statement.core.AlterSequenceStatement;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;


public class AlterSequenceGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<AlterSequenceStatement> {

    public AlterSequenceGeneratorHanaDBTest() throws Exception {
        this(new AlterSequenceGeneratorHanaDB());
    }

    protected AlterSequenceGeneratorHanaDBTest(SqlGenerator<AlterSequenceStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected AlterSequenceStatement createSampleSqlStatement() {
        AlterSequenceStatement alterSequenceStatement = new AlterSequenceStatement(null, null, "sequence_name");
//        alterSequenceStatement.setOrdered(true);
        alterSequenceStatement.setMinValue(new BigInteger("100"));
        return alterSequenceStatement;
    }


    @Test
    public void testAlterMinValue() throws Exception {
        super.isValid();
        AlterSequenceStatement alterSequenceStatement = new AlterSequenceStatement(null, null, "sequence_name");
        alterSequenceStatement.setMinValue(new BigInteger("1001"));

        Database hanadb = new HanaDBDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertFalse(generatorUnderTest.validate(alterSequenceStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
        Sql[] generatedSql = generatorUnderTest.generateSql(alterSequenceStatement, hanadb, sqlGeneratorChain);
        assertTrue(generatedSql.length == 1);
        assertEquals("ALTER SEQUENCE \"sequence_name\" MINVALUE 1001", generatedSql[0].toSql());

    }

    @Test
    public void testAlterIncrementByValue() throws Exception {
        super.isValid();
        AlterSequenceStatement alterSequenceStatement = new AlterSequenceStatement(null, null, "sequence_name");
        alterSequenceStatement.setIncrementBy(new BigInteger("5"));

        Database hanadb = new HanaDBDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertFalse(generatorUnderTest.validate(alterSequenceStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
        Sql[] generatedSql = generatorUnderTest.generateSql(alterSequenceStatement, hanadb, sqlGeneratorChain);
        assertTrue(generatedSql.length == 1);
        assertEquals("ALTER SEQUENCE \"sequence_name\" INCREMENT BY 5", generatedSql[0].toSql());

    }

    @Test
    public void testAlterOrdered() throws Exception {
        super.isValid();
        AlterSequenceStatement alterSequenceStatement = new AlterSequenceStatement(null, null, "sequence_name");
        alterSequenceStatement.setOrdered(true);

        Database hanadb = new HanaDBDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertTrue(generatorUnderTest.validate(alterSequenceStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
    }

}
