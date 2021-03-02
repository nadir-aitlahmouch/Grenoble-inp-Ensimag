package Interface;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.Vector;

public class JurysJTable extends JTable {
    public JurysJTable() {
    }

    public JurysJTable(TableModel dm) {
        super(dm);
    }

    public JurysJTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
    }

    public JurysJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }

    public JurysJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public JurysJTable(Vector<? extends Vector> rowData, Vector<?> columnNames) {
        super(rowData, columnNames);
    }

    public JurysJTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 2 ? true : false;
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            default:
                return Boolean.class;
        }
    }
};

