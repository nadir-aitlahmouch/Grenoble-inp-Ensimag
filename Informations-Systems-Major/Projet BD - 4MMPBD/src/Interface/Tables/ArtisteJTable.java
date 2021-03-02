package Interface.Tables;

import Interface.InputNumero;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.Vector;

public class ArtisteJTable extends JTable {
    public ArtisteJTable() {
    }

    public ArtisteJTable(TableModel dm) {
        super(dm);
    }

    public ArtisteJTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
    }

    public ArtisteJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }

    public ArtisteJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public ArtisteJTable(Vector<? extends Vector> rowData, Vector<?> columnNames) {
        super(rowData, columnNames);
    }

    public ArtisteJTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 3 ? true : false;
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            default:
                return Boolean.class;
        }
    }
};