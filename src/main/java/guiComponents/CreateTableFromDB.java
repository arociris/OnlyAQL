package guiComponents;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.json.simple.JSONObject;

import com.aerospike.client.query.KeyRecord;
import com.aerospike.client.query.RecordSet;

import onlyAQL.DBMain;

/**
 * Fetches data from db and create JTable filled with the data fetched from DB
 * 
 * @author njain
 *
 */

public class CreateTableFromDB {

	/**
	 * Fetches data from db and create JTable filled with the data fetched from
	 * DB. The data is parsed and presented in tabular format
	 * 
	 * @param dbConn
	 *            DB connection
	 * @param setName
	 *            Name of the set from DB
	 * @return JTable
	 * 
	 */
	public static JTable createTable(DBMain dbConn, String setName) {

		JTable table_1 = new JTable();
		// To enable the scrollbars in scrollpane
		table_1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		DefaultTableModel model = new DefaultTableModel();
		table_1.setModel(model);

		// Row column
		model.addColumn("  ");

		// Fetch records from DB
		RecordSet rs1 = dbConn.fetchsetRecords(setName);
		int currentRow = 0;

		// Setting up data in table
		for (KeyRecord keyRecord : rs1) {
			model.addRow(new Object[] {});

			// Adding data to columns of a row
			for (String s : keyRecord.record.bins.keySet()) {

				// Add column if not exists
				if (model.findColumn(s) == -1) {
					model.addColumn(s);
				}

				// get values from record and set it at the relevant cell
				try {

					model.setValueAt(keyRecord.record.getValue(s).toString(), currentRow,
							table_1.getColumn(s).getModelIndex());
				} catch (Exception e) {
					// In case value is null for a bin
					model.setValueAt(" ".toString(), currentRow, table_1.getColumn(s).getModelIndex());
				}
			}
			// set row number
			model.setValueAt(currentRow + 1, currentRow, table_1.getColumn("  ").getModelIndex());
			currentRow++;
		}

		// Attach a rowsorter to the table for filtering purpose
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table_1.getModel());
		table_1.setRowSorter(rowSorter);

		if (currentRow == 0) {
			return null;
		}
		table_1.getColumn("  ").setMaxWidth(35);
		table_1.getColumn("  ").setCellRenderer(new CellRenderer());

		return table_1;
	}

	/**
	 * Fetches data from db and create JTable filled with the data fetched from
	 * DB. The data is parsed and presented in JSON format
	 * 
	 * @param dbConn
	 *            DB connection
	 * @param setName
	 *            Name of the set from DB
	 * @return JTable
	 */
	public static JTable createJsonTable(DBMain dbConn, String setName) {

		JTable table_1 = new JTable();

		// To enable the scrollbars in scrollpane
		table_1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_1.setRowHeight(50);

		DefaultTableModel model = new DefaultTableModel();
		table_1.setModel(model);

		// Row column
		model.addColumn("  ");
		model.addColumn("Table Data");

		// Fetch records from DB
		RecordSet rs1 = dbConn.fetchsetRecords(setName);
		int currentRow = 0;

		// Setting up data in table
		for (KeyRecord keyRecord : rs1) {
			model.addRow(new Object[] {});

			//Convert data to JSON
			JSONObject json = new JSONObject(keyRecord.record.bins);
			
			//Set JSON string in table
			model.setValueAt(json, currentRow, table_1.getColumn("Table Data").getModelIndex());
			// set row number
			model.setValueAt(currentRow + 1, currentRow, table_1.getColumn("  ").getModelIndex());
			currentRow++;
		}

		// Attach a rowsorter to the table for filtering purpose
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table_1.getModel());
		table_1.setRowSorter(rowSorter);

		if (currentRow == 0) {
			return null;
		}
		table_1.getColumn("  ").setMaxWidth(35);
		table_1.getColumn("  ").setCellRenderer(new CellRenderer());
		table_1.getColumn("Table Data").setMinWidth(850);
		table_1.getColumn("Table Data").setCellRenderer(new TextAreaRenderer());

		return table_1;
	}
}
