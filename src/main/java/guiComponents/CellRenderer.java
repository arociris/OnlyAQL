package guiComponents;

/**
 * This will set the color of a specific cell in a table
 * @author njain
 *
 */
@SuppressWarnings("serial")
public class CellRenderer extends javax.swing.table.DefaultTableCellRenderer {

	public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, java.lang.Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		java.awt.Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);
		cellComponent.setBackground(java.awt.Color.LIGHT_GRAY);
		return cellComponent;
	}
}