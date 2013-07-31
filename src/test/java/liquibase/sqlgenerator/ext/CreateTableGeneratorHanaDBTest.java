package liquibase.sqlgenerator.ext;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.database.typeconversion.TypeConverterFactory;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.AbstractSqlGeneratorHanaDBTest;
import liquibase.sqlgenerator.MockSqlGeneratorChain;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.statement.ColumnConstraint;
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
        CreateTableStatement createTableStatement = new CreateTableStatement(null, "table_name");
        createTableStatement.addColumn("id",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int unsigned", false));
        return createTableStatement;
    }


    @Test
    public void testWithColumnWithDefaultValue() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, "TABLE_NAME");

        statement.addColumn("COLUMN1_NAME",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("java.sql.Types.TIMESTAMP", false),
                new ColumnConfig().setDefaultValue("null").getDefaultValueObject());

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" TIMESTAMP DEFAULT null)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }


    @Test
    public void testWithColumnSpecificIntType() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, "TABLE_NAME");

        statement.addColumn("COLUMN1_NAME",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int", false));

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" INTEGER)",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }


    @Test
    public void testCreateMultiColumnPrimary() throws Exception {
//        super.isValid();
        Database database = new HanaDBDatabase();

        CreateTableStatement createTableStatement = new CreateTableStatement(null, "table_name");
        createTableStatement.addColumn("id",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("VARCHAR(150)", false),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("author",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("VARCHAR(150)", false),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("dateExecuted",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("java.sql.Types.TIMESTAMP", false),
                new ColumnConfig().setDefaultValue("NULL").getDefaultValueObject());
        createTableStatement.addColumn("description",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("VARCHAR(255)", false));
        createTableStatement.addColumn("revision",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int", false));

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
        CreateTableStatement statement = new CreateTableStatement(null, "TABLE_NAME");

//        ColumnConfig column = new ColumnConfig();
//        column.setName("COLUMN1_NAME");
//        column.setType("int");
//        ConstraintsConfig constraints = new ConstraintsConfig();
//        constraints.setNullable(Boolean.FALSE);
//        constraints.setUnique(Boolean.TRUE);
//        column.setConstraints(constraints);

        UniqueConstraint uniqueConstraint = new UniqueConstraint();
        uniqueConstraint.setConstraintName("COLUMN1_UNIQUE");
        uniqueConstraint.addColumns("COLUMN1_NAME");

//        statement.addColumn("COLUMN1_NAME",
//                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int", false),
//                new ColumnConfig().setConstraints(constraints));
        statement.addColumn("COLUMN1_NAME",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int", false));

        statement.addColumnConstraint(uniqueConstraint);

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" INTEGER, UNIQUE (\"COLUMN1_NAME\"))",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

    @Test
    public void testWith2ColumnsUniqueConstraint() {
        Database database = new HanaDBDatabase();
        CreateTableStatement statement = new CreateTableStatement(null, "TABLE_NAME");

        UniqueConstraint uniqueConstraint = new UniqueConstraint();
        uniqueConstraint.setConstraintName("COLUMN1_UNIQUE");
        uniqueConstraint.addColumns("COLUMN1_NAME", "COLUMN2_NAME");

        statement.addColumn("COLUMN1_NAME",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("int", false));
        statement.addColumn("COLUMN2_NAME",
                TypeConverterFactory.getInstance().findTypeConverter(database).getDataType("varchar(100)", false));

        statement.addColumnConstraint(uniqueConstraint);

        assertEquals("CREATE TABLE \"TABLE_NAME\" (\"COLUMN1_NAME\" INTEGER, \"COLUMN2_NAME\" VARCHAR(100), UNIQUE (\"COLUMN1_NAME\", \"COLUMN2_NAME\"))",
                this.generatorUnderTest.generateSql(statement, database, null)[0].toSql());
    }

}
