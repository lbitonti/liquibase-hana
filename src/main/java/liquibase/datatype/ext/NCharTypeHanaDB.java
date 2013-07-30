package liquibase.datatype.ext;

import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.CharType;
import liquibase.datatype.core.NCharType;

@DataTypeInfo(name="nchar", aliases = "java.sql.Types.NCHAR",
        minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DATABASE)
public class NCharTypeHanaDB extends NCharType {


}