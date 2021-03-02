package Interface.Tables;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.Vector;

public class SpecialiteJTable extends JTable {
    public SpecialiteJTable() {
    }

    public SpecialiteJTable(TableModel dm) {
        super(dm);
    }

    public SpecialiteJTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
    }

    public SpecialiteJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }

    public SpecialiteJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public SpecialiteJTable(Vector<? extends Vector> rowData, Vector<?> columnNames) {
        super(rowData, columnNames);
    }

    public SpecialiteJTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 1 ? true : false;
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;

            default:
                return Boolean.class;
        }
    }
};
