
package onlyAQL;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.task.IndexTask;

public class UtilityService {
	private AerospikeClient client;

	public UtilityService(AerospikeClient c) {
		this.client = c;
	}

	public void createSecondaryIndexes() throws AerospikeException,
			InterruptedException {

    // NOTE: Index creation has been included in here for convenience and to demonstrate the syntax. The recommended way of creating indexes in production env is via AQL

		System.out.println("\nCreating secondary index on: set=tweets, bin=username...\n");
		IndexTask task1 = client.createIndex(null, "test", "tweets",
				"username_index", "username", IndexType.STRING);
		task1.waitTillComplete(100);
		System.out.println("Done creating secondary index on: set=tweets, bin=username\n");

		System.out.println("\nCreating secondary index on: set=tweets, bin=ts...\n");
		IndexTask task2 = client.createIndex(null, "test", "tweets", "ts_index",
				"ts", IndexType.NUMERIC);
		task2.waitTillComplete(100);
		System.out.println("Done creating secondary index on: set=tweets, bin=ts\n");

		System.out.println("\nCreating secondary index on: set=users, bin=tweetcount...\n");
		IndexTask task3 = client.createIndex(null, "test", "users",
				"tweetcount_index", "tweetcount", IndexType.NUMERIC);
		task3.waitTillComplete(100);
		System.out.println("Done creating secondary index on: set=users, bin=tweetcount\n");		
	}

	public static String printStackTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	/*
	 * Example functions not in use
	 */
	@SuppressWarnings("unused")
	private void add() throws AerospikeException {
		// Java Add
		Key userKey = new Key("test", "users", "user1234");
		Bin bin2 = new Bin("count", 3);
		client.add(null, userKey, bin2);
	}

	@SuppressWarnings("unused")
	private void append() throws AerospikeException {
		// Java Append
		Key userKey = new Key("test", "users", "user1234");
		Bin bin1 = new Bin("greet", "hello");
		Bin bin2 = new Bin("greet", " world");
		client.append(null, userKey, bin2);
	}

	@SuppressWarnings("unused")
	private void exists() throws AerospikeException {
		// Java Exists
		Key userKey = new Key("test", "users", "user1234");
		boolean recordKeyExists = client.exists(null, userKey);
	}

	@SuppressWarnings("unused")
	private void touch() throws AerospikeException {
		// Java Touch
		Key userKey = new Key("test", "users", "user1234");
		client.touch(null, userKey);
	}

}
