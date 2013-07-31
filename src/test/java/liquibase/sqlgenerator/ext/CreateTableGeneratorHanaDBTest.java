package liquibase.sqlgenerator.ext;

import liquibase.change.ColumnConfig;
import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.MockSqlGeneratorChain;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.UniqueConstraint;
import liquibase.statement.core.CreateTableStatement;
import org.junit.Test;


import static org.junit.Assert.*;


public class CreateTableGeneratorHanaDBTest extends AbstractSqlGeneratorHanaDBTest<CreateTableStatement> {

    public CreateTableGeneratorHanaDBTest() throws Exception {
        this(new CreateTableGeneratorHanaDB());
    }

    protected CreateTableGeneratorHanaDBTest(SqlGenerator<CreateTableStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected CreateTableStatement createSampleSqlStatement() {
        Database database = new HanaDBDatabase();
        CreateTableStatement createTableStatement = new CreateTableStatement(null, null, "table_name");
        createTableStatement.addColumn("id",
                DataTypeFactory.getInstance().fromDescription("int(11) unsigned"));
        return createTableStatement;
    }


    @Test
    public void testWithColumnWithDefaultValue() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, null, "TABLE_NAME");

        statement.addColumn("COLUMN1_NAME",
                DataTypeFactory.getInstance().fromDescription("java.sql.Types.TIMESTAMP"),
//                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("java.sql.Types.TIMESTAMP", false),
                new ColumnConfig().setDefaultValue("null").getDefaultValueObject());

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" TIMESTAMP DEFAULT null)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }


    @Test
    public void testWithColumnSpecificIntType() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, null, "TABLE_NAME");

        statement.addColumn("COLUMN1_NAME",
                DataTypeFactory.getInstance().fromDescription("int"));

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" INTEGER)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }


    @Test
    public void testCreateMultiColumnPrimary() throws Exception {
//        super.isValid();
        Database database = new HanaDBDatabase();

        CreateTableStatement createTableStatement = new CreateTableStatement(null, null, "table_name");
        createTableStatement.addColumn("id",
                DataTypeFactory.getInstance().fromDescription("VARCHAR(150)"),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("author",
                DataTypeFactory.getInstance().fromDescription("VARCHAR(150)"),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("dateExecuted",
                DataTypeFactory.getInstance().fromDescription("java.sql.Types.TIMESTAMP"),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("description",
                DataTypeFactory.getInstance().fromDescription("VARCHAR(255)"));
        createTableStatement.addColumn("revision",
                DataTypeFactory.getInstance().fromDescription("int"));

        Database hanadb = new HanaDBDatabase();
        SqlGeneratorChain sqlGeneratorChain = new MockSqlGeneratorChain();

        assertFalse(generatorUnderTest.validate(createTableStatement, hanadb, new MockSqlGeneratorChain()).hasErrors());
        Sql[] generatedSql = generatorUnderTest.generateSql(createTableStatement, hanadb, sqlGeneratorChain);
        assertTrue(generatedSql.length == 1);
        assertEquals("CREATE TABLE \"table_name\" (\"id\" VARCHAR(150) DEFAULT null, \"author\" VARCHAR(150) DEFAULT null, " +
                "\"dateExecuted\" TIMESTAMP DEFAULT null, \"description\" VARCHAR(255), \"revision\" INTEGER)",
                generatedSql[0].toSql());
    }


    @Test
    public void testWithColumnUniqueConstraint() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, null, "TABLE_NAME");

        UniqueConstraint uniqueConstraint = new UniqueConstraint();
        uniqueConstraint.setConstraintName("COLUMN1_UNIQUE");
        uniqueConstraint.addColumns("COLUMN1_NAME");

        statement.addColumn("COLUMN1_NAME",
                DataTypeFactory.getInstance().fromDescription("int"));

        statement.addColumnConstraint(uniqueConstraint);

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" INTEGER, UNIQUE (\"COLUMN1_NAME\"))",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testWith2ColumnsUniqueConstraint() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, null, "TABLE_NAME");

        UniqueConstraint uniqueConstraint = new UniqueConstraint();
        uniqueConstraint.setConstraintName("COLUMN1_UNIQUE");
        uniqueConstraint.addColumns("COLUMN1_NAME", "COLUMN2_NAME");

        statement.addColumn("COLUMN1_NAME",
                DataTypeFactory.getInstance().fromDescription("int"));
        statement.addColumn("COLUMN2_NAME",
                DataTypeFactory.getInstance().fromDescription("varchar(100)"));

        statement.addColumnConstraint(uniqueConstraint);

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" INTEGER, \"COLUMN2_NAME\" VARCHAR(100), UNIQUE (\"COLUMN1_NAME\", \"COLUMN2_NAME\"))",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
