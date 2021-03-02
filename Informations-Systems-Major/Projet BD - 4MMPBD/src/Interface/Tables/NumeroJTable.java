package Interface.Tables;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.Vector;

public class NumeroJTable extends JTable {
    public NumeroJTable() {
    }

    public NumeroJTable(TableModel dm) {
        super(dm);
    }

    public NumeroJTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
    }

    public NumeroJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }

    public NumeroJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public NumeroJTable(Vector<? extends Vector> rowData, Vector<?> columnNames) {
        super(rowData, columnNames);
    }

    public NumeroJTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 1:
                return String.class;

            default:
                return Integer.class;
        }
    }
}
