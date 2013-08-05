package liquibase.snapshot.jvm.ext;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.SnapshotGeneratorChain;
import liquibase.snapshot.jvm.SequenceSnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;

public class SequenceSnapshotGeneratorHana extends SequenceSnapshotGenerator {

	@Override
	protected String getSelectSequenceSql(Schema schema, Database database) {
		if (database instanceof HanaDBDatabase) {
			return "SELECT SEQUENCE_NAME FROM SYS.SEQUENCES WHERE SCHEMA_NAME = '"
					+ schema.getName() + "'";
		}
		return super.getSelectSequenceSql(schema, database);
	}

	@Override
	public DatabaseObject snapshot(DatabaseObject example,
			DatabaseSnapshot snapshot, SnapshotGeneratorChain chain)
			throws DatabaseException, InvalidExampleException {
		if (!(example instanceof Table || example instanceof Schema)) {
			// call chain.snapshot to skip SequenceSnapshotGenerator
			return chain.snapshot(example, snapshot);
		}
		// do not call super.snapshot( , , chain) to prevent
		// SequenceSnapshotGenerator to be called
		return super.snapshotObject(example, snapshot);
	}

	@Override
	public int getPriority(Class<? extends DatabaseObject> objectType,
			Database database) {
		int priority = super.getPriority(objectType, database);
		return priority + ((priority > 0) && database instanceof HanaDBDatabase ? 1 : 0);
	}

}
