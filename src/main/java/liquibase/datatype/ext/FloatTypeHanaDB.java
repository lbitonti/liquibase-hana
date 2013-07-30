package liquibase.datatype.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.FloatType;

@DataTypeInfo(name="float", aliases = {"java.sql.Types.FLOAT","java.lang.Float"},
        minParameters = 0, maxParameters = 2, priority = LiquibaseDataType.PRIORITY_DATABASE)
public class FloatTypeHanaDB extends FloatType {

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof HanaDBDatabase) {
            return new DatabaseDataType("FLOAT");
        }
        return super.toDatabaseDataType(database);
    }

}
