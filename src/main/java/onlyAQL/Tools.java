package onlyAQL;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.query.KeyRecord;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;

/**
 * This class has methods and utilities which can be used for various purposes 
 * @author njain
 *
 */
public class Tools {

	
/**
 * Will parse the string and return the list of sets
 * @param input - Output string on asinfo command
 * @return
 */
	public static List<String> getSetsFromString(String input) {
		List<String> sets = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(input, ";");
		while (st.hasMoreTokens()) {
			String setName = null;
			String[] setEntities = st.nextToken().split(":");
			for (String setEntity : setEntities) {
				if (setEntity.startsWith("ns=")) {
					setName = setEntity.substring(3);
				}
				if (setEntity.startsWith("set=")) {
					setName = setName + "." + setEntity.substring(4);
				}
			}
			sets.add(setName);
		}
		return sets;
	}



	public static String getNameSpace(String set) {
		return set.split("\\.")[0];

	}

	public static String getSet(String set) {
		return set.split("\\.")[1];

	}

	public static Set<String> diffBinList(final Set<String> set1, final Set<String> set2) {
		if (Objects.isNull(set1)) {
			return set2;
		}

			set1.addAll(set2);

		return set1;
	}

	public static RecordSet fetchQueryResult(AerospikeClient client, String set) {
		Statement stmt = new Statement();
		stmt.setNamespace(Tools.getNameSpace(set));
		stmt.setSetName(Tools.getSet(set));
		QueryPolicy qPolicy = new QueryPolicy();
		qPolicy.sendKey = true;
		RecordSet rs = client.query(qPolicy, stmt);
		
		return rs;
	}

	public static Set<Object> objectFromKeyRecord(KeyRecord keyRecord, String[] binList) {

		Set<Object> obj1 = new HashSet<Object>();
		for (String s : binList) {
			try {
				obj1.add(keyRecord.record.getValue(s).toString());
			} catch (Exception e) {
				obj1.add("NA");
			}
		}

		return obj1;
	}
	
	/**
	 * This method will write data from JTable to the path provided
	 * @param table
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeToExcell(JTable table, Path path) throws FileNotFoundException, IOException {

		new WorkbookFactory();
		Workbook wb = new XSSFWorkbook(); // Excel workbook
		Sheet sheet = (Sheet) wb.createSheet(); // WorkSheet
		Row row = sheet.createRow(1); 
		TableModel model = table.getModel(); // Table model

		Row headerRow = sheet.createRow(0); // Create row at line 0
		for (int headings = 0; headings < model.getColumnCount(); headings++) { // For each column
			// Write column name															
			headerRow.createCell(headings).setCellValue(model.getColumnName(headings));
		}

		for (int rows = 0; rows < model.getRowCount(); rows++) { // For each table row
			// For each table column											
			for (int cols = 0; cols < table.getColumnCount(); cols++) { 
				// Write value
				if(Objects.isNull(model.getValueAt(rows, cols))){
					row.createCell(cols).setCellValue("");
					continue;
				}
				row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); 
			}

			// Set the row to the next one in the sequence
			row = sheet.createRow((rows + 2));
		}
		wb.write(new FileOutputStream(path.toString()));// Save the file
	}

}
