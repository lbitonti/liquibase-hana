package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.MockSqlGeneratorChain;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.core.AddDefaultValueStatement;
import liquibase.statement.core.AddPrimaryKeyStatement;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddPrimaryKeyGeneratorHanaDBTest {

    @Test
    public void testIsNotSupported() throws Exception {
        AddPrimaryKeyGeneratorHanaDB generatorUnderTest = new AddPrimaryKeyGeneratorHanaDB();

        AddPrimaryKeyStatement addPrimaryKeyStatement =
                new AddPrimaryKeyStatement(null, "table_name", "column_name", "pk_constraint");

        assertFalse(generatorUnderTest.supports(addPrimaryKeyStatement, new HanaDBDatabase()));
    }

}

//public class AddPrimaryKeyGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<AddPrimaryKeyStatement> {
//
//    public AddPrimaryKeyGeneratorHanaDBTest() throws Exception {
//        this(new AddPrimaryKeyGeneratorHanaDB());
//    }
//
//    protected AddPrimaryKeyGeneratorHanaDBTest(SqlGenerator<AddPrimaryKeyStatement> generatorUnderTest) throws Exception {
//        super(generatorUnderTest);
//    }
//
//	@Override
//	protected AddPrimaryKeyStatement createSampleSqlStatement() {
//		return new AddPrimaryKeyStatement(null, "table_name", "column_name", "pk_constraint");
//	}
//
//
//    @Test
//    public void testAddPrimaryKey() throws Exception {
//        super.isValid();
//        AddPrimaryKeyStatement addPrimaryKeyStatement =
//                new AddPrimaryKeyStatement(null, "table_name", "column_name", "pk_constraint");
//
//        Database hanadb = new HanaDBDatabase();
//        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();
//
//        assertFalse(generatorUnderTest.validate(addPrimaryKeyStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
//        Sql[] generatedSql = generatorUnderTest.generateSql(addPrimaryKeyStatement, hanadb, sqlGeneratorChain);
//        assertTrue(generatedSql.length == 1);
//        assertEquals("ALTER TABLE \"table_name\" ALTER (\"column_name\" PRIMARY KEY)", generatedSql[0].toSql());
//
//    }
//
//    @Test
//    public void testAddPrimaryKeyWithTableSpace() throws Exception {
//        super.isValid();
//        AddPrimaryKeyStatement addPrimaryKeyStatement =
//                new AddPrimaryKeyStatement("table_space", "table_name", "column_name", "pk_constraint");
//
//        Database hanadb = new HanaDBDatabase();
//        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();
//
//        assertFalse(generatorUnderTest.validate(addPrimaryKeyStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
//        Sql[] generatedSql = generatorUnderTest.generateSql(addPrimaryKeyStatement, hanadb, sqlGeneratorChain);
//        assertTrue(generatedSql.length == 1);
//        assertEquals("ALTER TABLE \"table_space\".\"table_name\" ALTER (\"column_name\" PRIMARY KEY)", generatedSql[0].toSql());
//
//    }
//
//}
