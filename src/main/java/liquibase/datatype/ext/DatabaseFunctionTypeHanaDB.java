package liquibase.datatype.ext;

import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.DatabaseFunctionType;
import liquibase.statement.DatabaseFunction;

@DataTypeInfo(name="function", aliases = "liquibase.statement.DatabaseFunction",
        minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DATABASE)
public class DatabaseFunctionTypeHanaDB extends DatabaseFunctionType {

}
