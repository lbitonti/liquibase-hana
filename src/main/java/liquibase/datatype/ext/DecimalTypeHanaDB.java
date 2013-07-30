package liquibase.datatype.ext;

import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.DecimalType;

@DataTypeInfo(name="decimal", aliases = "java.sql.Types.DECIMAL" , minParameters = 0, maxParameters = 2,
        priority = LiquibaseDataType.PRIORITY_DATABASE)
public class DecimalTypeHanaDB extends DecimalType {
}
