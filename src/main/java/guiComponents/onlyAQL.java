package guiComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.aerospike.client.AerospikeException;

import onlyAQL.DBMain;
import onlyAQL.Tools;

/**
 * This will launch onlyAQL main class and the GUI
 * 
 * @author njain
 *
 */
@SuppressWarnings("unused")
public class onlyAQL {
	private JFrame frmOnlyaql;
	private JTextField txtEnterServerIp;
	private JTextField txtServerPort;

	private JTable table;
	private JTable table_1;
	JButton dbSubmit = new JButton("Connect");
	JButton dbDisconnect = new JButton("Disconnect");
	static JButton fetchFrmDB = new JButton("Fetch");
	static JButton refreshTable = new JButton("refresh");
	static JButton exportToExcel = new JButton("Export to Excel");
	JComboBox<String> comboBox = new JComboBox<String>();
	static JLabel tableFltr_lbl = new JLabel("Search table data:");
	
	DBMain dbConn;
	private static JTextField tblFltr_textField;
	private JTable table_2;
	private ButtonGroup radioBtnGrp = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					onlyAQL window = new onlyAQL();
					window.frmOnlyaql.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor
	 */
	public onlyAQL() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame and display the window
	 */
	private void initialize() {

		// Main frame
		frmOnlyaql = new JFrame();
		frmOnlyaql.setTitle("onlyAQL");
		frmOnlyaql.setBounds(100, 100, 1024, 544);
		frmOnlyaql.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Top Panel
		JPanel panel = new JPanel();
		frmOnlyaql.getContentPane().add(panel, BorderLayout.NORTH);

		// Layout of topPanel
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 30, 97, 97, 10, 83, 84, 150, 30, 30, 30, 30, 0, 30, 0 };
		gbl_panel.rowHeights = new int[] { 25, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		// Server IP field
		txtEnterServerIp = new JTextField();

		/*
		 * Logic to autoclear server IP address box
		 */
		txtEnterServerIp.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtEnterServerIp.getText().equals("<Server IP>")) {
					txtEnterServerIp.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (Objects.isNull(txtEnterServerIp.getText()) || txtEnterServerIp.getText().equals("")) {
					txtEnterServerIp.setText("<Server IP>");
				}
			}
		});
		txtEnterServerIp.setToolTipText("Enter server IP address");
		GridBagConstraints gbc_txtEnterServerIp = new GridBagConstraints();
		gbc_txtEnterServerIp.fill = GridBagConstraints.BOTH;
		gbc_txtEnterServerIp.insets = new Insets(0, 0, 0, 5);
		gbc_txtEnterServerIp.gridx = 1;
		gbc_txtEnterServerIp.gridy = 0;
		panel.add(txtEnterServerIp, gbc_txtEnterServerIp);
		txtEnterServerIp.setColumns(10);

		/*
		 * Logic to autoclear server port address box
		 */
		txtServerPort = new JTextField();
		txtServerPort.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtServerPort.getText().equals("3000")) {
					txtServerPort.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (Objects.isNull(txtServerPort.getText()) || txtServerPort.getText().equals("")) {
					txtServerPort.setText("3000");
				}
			}
		});
		txtServerPort.setText("3000"); // Default server port
		txtServerPort.setToolTipText("Enter server port. Default value 3000");
		GridBagConstraints gbc_txtServerPort = new GridBagConstraints();
		gbc_txtServerPort.fill = GridBagConstraints.BOTH;
		gbc_txtServerPort.insets = new Insets(0, 0, 0, 5);
		gbc_txtServerPort.gridx = 2;
		gbc_txtServerPort.gridy = 0;
		panel.add(txtServerPort, gbc_txtServerPort);
		txtServerPort.setColumns(10);

		// DB Submit button
		dbSubmit.setVerticalAlignment(SwingConstants.BOTTOM);
		dbSubmit.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_dbSubmit = new GridBagConstraints();
		gbc_dbSubmit.fill = GridBagConstraints.BOTH;
		gbc_dbSubmit.insets = new Insets(0, 0, 0, 5);
		gbc_dbSubmit.gridx = 3;
		gbc_dbSubmit.gridy = 0;
		panel.add(dbSubmit, gbc_dbSubmit);

		// DB Disconnect button
		dbDisconnect.setVerticalAlignment(SwingConstants.BOTTOM);
		dbDisconnect.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(dbDisconnect, gbc_dbSubmit);
		dbDisconnect.setVisible(false);

		// Radio Buttons
		JRadioButton jsonRadio = new JRadioButton("JSON");
		jsonRadio.setActionCommand("json");
		GridBagConstraints gbc_jsonRadio = new GridBagConstraints();
		gbc_jsonRadio.fill = GridBagConstraints.HORIZONTAL;
		gbc_jsonRadio.insets = new Insets(0, 0, 0, 5);
		gbc_jsonRadio.gridx = 4;
		gbc_jsonRadio.gridy = 0;
		panel.add(jsonRadio, gbc_jsonRadio);
		radioBtnGrp.add(jsonRadio);
		jsonRadio.setVisible(false);

		// Fetch from db button
		fetchFrmDB.setVisible(false);

		JRadioButton tableRadio = new JRadioButton("Table");
		tableRadio.setActionCommand("table");
		GridBagConstraints gbc_tableRadio = new GridBagConstraints();
		gbc_tableRadio.fill = GridBagConstraints.HORIZONTAL;
		gbc_tableRadio.insets = new Insets(0, 0, 0, 5);
		gbc_tableRadio.gridx = 5;
		gbc_tableRadio.gridy = 0;
		panel.add(tableRadio, gbc_tableRadio);

		tableRadio.setSelected(true);
		radioBtnGrp.add(tableRadio);
		tableRadio.setVisible(false);
		comboBox.setEditable(true);		
		comboBox.setEditable(true);

		// Set list combobox
		comboBox.setVisible(false);
		comboBox.setSelectedIndex(-1);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.BOTH;
		gbc_comboBox.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox.gridx = 6;
		gbc_comboBox.gridy = 0;
		panel.add(comboBox, gbc_comboBox);
		
		GridBagConstraints gbc_fetchFrmDB = new GridBagConstraints();
		gbc_fetchFrmDB.insets = new Insets(0, 0, 0, 5);
		gbc_fetchFrmDB.gridx = 7;
		gbc_fetchFrmDB.gridy = 0;
		panel.add(fetchFrmDB, gbc_fetchFrmDB);

		// Refresh table button
		refreshTable.setVisible(false);
		GridBagConstraints gbc_refreshTable = new GridBagConstraints();
		gbc_refreshTable.insets = new Insets(0, 0, 0, 5);
		gbc_refreshTable.gridwidth = 3;
		gbc_refreshTable.fill = GridBagConstraints.BOTH;
		gbc_refreshTable.gridx = 8;
		gbc_refreshTable.gridy = 0;
		panel.add(refreshTable, gbc_refreshTable);


		// Tabbed pane, the main container for tables
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		frmOnlyaql.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		// Bottom toolar
		JToolBar toolBar = new JToolBar();
		toolBar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		toolBar.setFloatable(false);
		frmOnlyaql.getContentPane().add(toolBar, BorderLayout.SOUTH);

		// Export to excel button
		exportToExcel.setVisible(false);
		exportToExcel.setBackground(Color.LIGHT_GRAY);

		/*
		 * Logic for export to excel button
		 */
		exportToExcel.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if (!exportToExcel.isEnabled()) {
					return;
				}
				// Select current table
				JTable selectedTable = (JTable) ((JScrollPane) tabbedPane.getSelectedComponent()).getViewport()
						.getComponent(0);

				Path path = null;
				// Open the FIle choose dialog
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(tabbedPane);
				if (result == JFileChooser.APPROVE_OPTION) {
					JLabel msgLbl = new JLabel();
					msgLbl.setText("Export to excel in progress");
					path = Paths.get(fileChooser.getSelectedFile().getAbsolutePath());

					/*
					 * Showing alert message to the user about export in
					 * progress
					 */
					exportToExcel.setEnabled(false);
					Thread t = new Thread(new Runnable() {
						public void run() {
							JOptionPane.showOptionDialog(tabbedPane, msgLbl, "Alert", JOptionPane.NO_OPTION,
									JOptionPane.INFORMATION_MESSAGE, null, new Object[] {}, null);
						}
					});
					t.start();

					// Attempting to write to excel
					try {
						Tools.writeToExcell(selectedTable, path);
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(tabbedPane, "File is not present");
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(tabbedPane,
								"Unable to write to the file due to \n" + e1.getMessage());
					} finally {
						// Killing the the alert message and enabling the export
						// to excel button
						Window win = SwingUtilities.getWindowAncestor(msgLbl);
						win.setVisible(false);
						t.stop();
						exportToExcel.setEnabled(true);

					}

				}

			}
		});

		// Table filter
		tableFltr_lbl.setVisible(false);
		tableFltr_lbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
		toolBar.add(tableFltr_lbl);

		tblFltr_textField = new JTextField();
		tblFltr_textField.setVisible(false);
		tblFltr_textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		toolBar.add(tblFltr_textField);
		tblFltr_textField.setColumns(1000);

		JLabel seperatorLbl = new JLabel("                                                                         ");
		toolBar.add(seperatorLbl);
		toolBar.add(exportToExcel);

		// The menue bar
		JMenuBar menuBar = new JMenuBar();
		frmOnlyaql.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("OnlyAQL");
		mnNewMenu.setHorizontalAlignment(SwingConstants.RIGHT);
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnNewMenu.add(mntmNewMenuItem);

		// Logic to fetch a table from DB
		fetchFrmDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dataFormat = radioBtnGrp.getSelection().getActionCommand();
				createTable(comboBox.getSelectedItem().toString(),dataFormat);


			}

			// Logic to fetch table via CreateTableFromDB and adding it to
			// screen
			public void createTable(String setName,String format) {
				JScrollPane scrollPane = new JScrollPane((Component) null);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

				if(format.equals("table")){
					table_1 = CreateTableFromDB.createTable(dbConn, setName);
				}else if(format.equals("json")){
					table_1 = CreateTableFromDB.createJsonTable(dbConn, setName);
				}
					
				
				if (Objects.isNull(table_1)) {
					JOptionPane.showMessageDialog(tabbedPane, "No any records are present");
					return;
				}

				tabbedPane.addTab(setName, null, scrollPane, null);

				scrollPane.setViewportView(table_1);
				tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
				tabbedPane.getSelectedIndex();
				tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1,
						getTitlePanel(tabbedPane, scrollPane, setName));
				JOptionPane.showMessageDialog(tabbedPane, "Found " + table_1.getRowCount() + " records");
				enableTableComponents();

			}

		});

		// Logic to for db connection button action
		dbSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// createTable();
				String serverIp = txtEnterServerIp.getText();
				int port;
				try {
					port = Integer.parseInt(txtServerPort.getText());
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
					return;
				}

				txtEnterServerIp.getText();
				if (!(serverIp.equals("<Server IP>"))) {
					try {
						// Connect to the DB
						dbConn = new DBMain(serverIp, port);
					} catch (AerospikeException e1) {
						JOptionPane.showMessageDialog(tabbedPane,
								"Unable to connect to the server :" + e1.getMessage());
						return;
					}

					try {
						// Check connection state
						if (!dbConn.isConnected()) {
							return;
						}
					} catch (AerospikeException e1) {
						JOptionPane.showMessageDialog(tabbedPane, "AerospikeException - Message: " + e1.getMessage());
						return;
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(tabbedPane, "Exception - Message: " + e1.getMessage());
						return;
					}

					// Fetching ser names
					List<String> setList = dbConn.fetchSetNames();
					Collections.sort(setList);

					// Preparing dropdown list of sets
					for (String setid : setList) {
						comboBox.addItem(setid);
					}

					// Adding autocomplete to the combobox
					AutoCompleteDecorator.decorate(comboBox);
					comboBox.setSelectedIndex(0);
					comboBox.setVisible(true);

					// Enabling other fields
					fetchFrmDB.setVisible(true);
					dbSubmit.setVisible(false);
					dbDisconnect.setVisible(true);

					jsonRadio.setVisible(true);
					tableRadio.setVisible(true);

					// Case of reconnect in case a tab is open
					if (tabbedPane.getTabCount() > 0) {
						refreshTable.setVisible(true);
					}

				}

			}

		});

		// Logic to for db disconnect button action
		dbDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// Attempt disconnection
					dbConn.TerminateConnection();
				} catch (AerospikeException e1) {
					JOptionPane.showMessageDialog(tabbedPane, "Unable to disconnect -" + e1.getMessage());
					return;
				}

				try {
					// Check connection state
					if (dbConn.isConnected()) {
						JOptionPane.showMessageDialog(tabbedPane, "Unable to disconnect");
						return;
					}
				} catch (AerospikeException e1) {
					JOptionPane.showMessageDialog(tabbedPane, "AerospikeException - Message: " + e1.getMessage());
					return;
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(tabbedPane, "Exception - Message: " + e1.getMessage());
					return;
				}

				// Disabling fields
				comboBox.setVisible(false);
				fetchFrmDB.setVisible(false);
				refreshTable.setVisible(false);
				dbSubmit.setVisible(true);
				dbDisconnect.setVisible(false);
				jsonRadio.setVisible(false);
				tableRadio.setVisible(false);
			}
		});


		// Logic to refresh the table
		refreshTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String setName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
				JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
				if(radioBtnGrp.getSelection().getActionCommand().equals("table")){
					table_1 = CreateTableFromDB.createTable(dbConn, setName);
				}else if(radioBtnGrp.getSelection().getActionCommand().equals("json")){
					table_1 = CreateTableFromDB.createJsonTable(dbConn, setName);
				}				
				if (Objects.isNull(table_1)) {
					JOptionPane.showMessageDialog(tabbedPane, "No records found");
					tabbedPane.remove(scrollPane);
					return;
				}
				scrollPane.setViewportView(table_1);
				JOptionPane.showMessageDialog(tabbedPane, "Found " + table_1.getRowCount() + " records");

			}
		});
		// Logic for the filtering data in table
		tblFltr_textField.getDocument().addDocumentListener(new DocumentListener() {

			TableRowSorter<TableModel> rowSorter;

			@SuppressWarnings("unchecked")
			@Override
			public void insertUpdate(DocumentEvent e) {

				// Fetch rowsorter object for current selected table
				rowSorter = (TableRowSorter<TableModel>) ((JTable) ((JViewport) (((JScrollPane) tabbedPane
						.getSelectedComponent()).getComponent(0))).getComponent(0)).getRowSorter();
				String text = tblFltr_textField.getText();

				// Set the filter
				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));

				}

			}

			@SuppressWarnings("unchecked")
			@Override
			public void removeUpdate(DocumentEvent e) {
				// Fetch rowsorter object for current selected table
				rowSorter = (TableRowSorter<TableModel>) ((JTable) ((JViewport) (((JScrollPane) tabbedPane
						.getSelectedComponent()).getComponent(0))).getComponent(0)).getRowSorter();
				String text = tblFltr_textField.getText();

				// Set the filter
				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// Not required. do nothing

			}

		});

	}

	/**
	 * This method crates title panel for the newly create tab. This panel will
	 * have close button which can close the tab.
	 * 
	 * @param tabbedPane
	 *            - Owner Tabbed pane of the new tab
	 * @param scroll
	 *            - Scrollpane to be removed on closure of tab
	 * @param title
	 *            - Title of the tab
	 * @return - Title panel with name and close button
	 */
	private static JPanel getTitlePanel(final JTabbedPane tabbedPane, final JScrollPane scroll, String title) {
		JPanel titlePanel = new JPanel();
		titlePanel.setOpaque(false);

		JLabel titleLbl = new JLabel(title);
		titleLbl.setFont(new Font("Tahoma", Font.PLAIN, 13));
		// titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		titlePanel.add(titleLbl);
		JButton closeButton = new JButton("x");
		closeButton.setFont(new Font("Tahoma", Font.PLAIN, 9));

		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.remove(scroll);
				if (tabbedPane.getTabCount() == 0) {
					disableTableComponents();
				}
			}
		});
		titlePanel.add(closeButton);

		return titlePanel;
	}

	/**
	 * This method will disbale table components which should be visible only in
	 * case a table in displayed
	 */
	protected static void disableTableComponents() {

		exportToExcel.setVisible(false);
		tableFltr_lbl.setVisible(false);
		tblFltr_textField.setVisible(false);
		refreshTable.setVisible(false);

	}

	/**
	 * This method will enable table components which should be visible only in
	 * case a table in displayed
	 */

	protected static void enableTableComponents() {

		exportToExcel.setVisible(true);
		tableFltr_lbl.setVisible(true);
		tblFltr_textField.setVisible(true);
		refreshTable.setVisible(true);

	}

}
