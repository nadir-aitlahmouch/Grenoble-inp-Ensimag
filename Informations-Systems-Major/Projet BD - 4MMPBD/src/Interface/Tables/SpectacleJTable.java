package Interface.Tables;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.Vector;

public class SpectacleJTable extends JTable {
    public SpectacleJTable() {
    }

    public SpectacleJTable(TableModel dm) {
        super(dm);
    }

    public SpectacleJTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
    }

    public SpectacleJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }

    public SpectacleJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public SpectacleJTable(Vector<? extends Vector> rowData, Vector<?> columnNames) {
        super(rowData, columnNames);
    }

    public SpectacleJTable(Object[][] rowData, Object[] columnNames) {
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
            default:
                return String.class;
        }
    }
};
