package liquibase.database.datatype.ext.hanadb;

import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.CharType;


@DataTypeInfo(name="char", aliases = "java.sql.Types.CHAR",
        minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DATABASE)
public class CharTypeHanaDB extends CharType {

}
