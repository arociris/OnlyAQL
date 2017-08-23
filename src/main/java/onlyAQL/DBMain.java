package onlyAQL;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Info;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.KeyRecord;
import com.aerospike.client.query.RecordSet;

/**
 * This will connect to the DB and maintain the connection. all DB operations
 * are performed via this class only
 * 
 * @author njain
 *
 */
public class DBMain {
	private AerospikeClient client;

	private String seedHost;
	private int port;
	private WritePolicy writePolicy;
	private Policy policy;

	/**
	 * Will create a new DB connection with the server host and port
	 * 
	 * @param host
	 * @param port
	 */
	public DBMain(String host, int port) {
		this.seedHost = host;
		this.port = port;
		this.writePolicy = new WritePolicy();
		// this.writePolicy.timeout = 100;
		this.writePolicy.setTimeout(1000);
		this.policy = new Policy();
		this.policy.setTimeout(1000);
		// Establish a connection to Aerospike cluster
		ClientPolicy cPolicy = new ClientPolicy();
		cPolicy.timeout = 5000;

		// Connecting to the aerospike db
		this.client = new AerospikeClient(cPolicy, this.seedHost, this.port);
		client.readPolicyDefault.sendKey = true;
		// client.writePolicyDefault.sendKey = true;
		client.scanPolicyDefault.sendKey = true;
		client.queryPolicyDefault.sendKey = true;

	}

	/**
	 * This will check the connection state
	 * 
	 * @return
	 * @throws Exception
	 * @throws AerospikeException
	 */
	public boolean isConnected() throws Exception, AerospikeException {
		if (client == null || !client.isConnected()) {
			return false;
		} else {
			return true;
		}

	}

	public AerospikeClient getClient() {
		return client;
	}

	protected void finalize() throws Throwable {
		if (this.client != null) {
			this.client.close();
		}
	}

	/**
	 * This will fetch the list of columns for a table
	 * 
	 * @param setName
	 * @return
	 */
	public Set<String> fetchBinList(String setName) {
		RecordSet rs = Tools.fetchQueryResult(client, setName);
		Set<String> binList = new TreeSet<>();
		binList.add("  ");
		for (KeyRecord keyRecord : rs) {
			binList = Tools.diffBinList(binList, keyRecord.record.bins.keySet());

		}
		if (binList != null) {

			for (String s : binList) {
			}

			return binList;
		}
		return null;

	}

	/**
	 * This will fetch all records for mentioned set
	 * 
	 * @param setName
	 * @return
	 */
	public RecordSet fetchsetRecords(String setName) {
		RecordSet rs1 = Tools.fetchQueryResult(client, setName);

		return rs1;

	}

	/**
	 * This will fetch list of all sets in a DB
	 * 
	 * @return
	 */
	public List<String> fetchSetNames() {
		InfoPolicy iPolicy = new InfoPolicy();

		return Tools.getSetsFromString(Info.request(iPolicy, client.getNodes()[0], "sets"));

	}

	
	/*
	 * example method calls

	public Record readPartial(String userName) throws AerospikeException {
		// Java read specific bins
		Key key = new Key("test", "users", userName);
		Record record = this.client.get(null, key, "username", "password", "gender", "region");
		return record;
	}

	public Record readMeta(String userName) throws AerospikeException {
		// Java get meta data
		Key key = new Key("test", "users", userName);
		Record record = this.client.getHeader(null, key);
		return record;
	}

	public void write(String username, String password) throws AerospikeException {
		// Java read-modify-write
		WritePolicy wPolicy = new WritePolicy();
		wPolicy.generationPolicy = GenerationPolicy.EXPECT_GEN_EQUAL;

		Key key = new Key("test", "users", username);
		Bin bin1 = new Bin("username", username);
		Bin bin2 = new Bin("password", password);

		client.put(wPolicy, key, bin1, bin2);
	}

	public void delete(String username) throws AerospikeException {
		// Java Delete record.
		WritePolicy wPolicy = new WritePolicy();
		Key key = new Key("test", "users", username);
		client.delete(wPolicy, key);
	}

	public boolean exisis(String username) throws AerospikeException {
		// Java exists
		Key key = new Key("test", "users", username);
		boolean itsHere = client.exists(policy, key);
		return itsHere;
	}

	public void add(String username) throws AerospikeException {
		// Java add
		WritePolicy wPolicy = new WritePolicy();
		Key key = new Key("test", "users", username);
		Bin counter = new Bin("tweetcount", 1);
		client.add(wPolicy, key, counter);
	}

	public void touch(String username) throws AerospikeException {
		// Java touch
		WritePolicy wPolicy = new WritePolicy();
		Key key = new Key("test", "users", username);
		client.touch(wPolicy, key);
	}

	public void append(String username) throws AerospikeException {
		// Java append
		WritePolicy wPolicy = new WritePolicy();
		Key key = new Key("test", "users", username);
		Bin bin2 = new Bin("interests", "cats");
		client.append(wPolicy, key, bin2);
	}

	public void operate(String username) throws AerospikeException {
		// Java operate
		WritePolicy wPolicy = new WritePolicy();
		Key key = new Key("test", "users", username);
		client.operate(wPolicy, key, Operation.put(new Bin("tweetcount", 153)),
				Operation.put(new Bin("lasttweeted", 1406755079L)));

	}

	@SuppressWarnings("unused")
	public void batch(String username) throws AerospikeException {
		// Java batch
		// Create an array of keys so we can initiate batch read operation
		Key[] keys = new Key[27];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = new Key("test", "tweets", (username + ":" + (i + 1)));
		}

		// Initiate batch read operation
		Record[] records = client.get(null, keys);

	}

	@SuppressWarnings({ "unused", "resource" })
	public void multipleSeedNodes() throws AerospikeException {
		Host[] hosts = new Host[] { new Host("a.host", 3000), new Host("another.host", 3000),
				new Host("and.another.host", 3000) };
		AerospikeClient client = new AerospikeClient(new ClientPolicy(), hosts);

	}

	@SuppressWarnings({ "unused", "resource" })
	public void connectWithClientPolicy() throws AerospikeException {
		// Java connection with Client policy
		ClientPolicy clientPolicy = new ClientPolicy();
		// clientPolicy.maxThreads = 200; //200 threads
		clientPolicy.maxSocketIdle = 3; // 3 seconds
		AerospikeClient client = new AerospikeClient(clientPolicy, "a.host", 3000);

	}

	public void deleteBin(String username) throws AerospikeException {
		// Java delete a bin
		WritePolicy wPolicy = new WritePolicy();
		Key key = new Key("test", "users", username);
		Bin bin1 = Bin.asNull("shoe-size"); // Set bin value to null to drop
											// bin.
		client.put(wPolicy, key, bin1);
	}

*/
	
}
