package Interface.Model;

import Transaction.RequestCB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by lial on 11/27/19.
 */
public class SpectacleModel extends DefaultTableModel{
    public SpectacleModel(Connection conn){
        super();
        this.addColumn("Code Spectacle");
        this.addColumn("Date");
        this.addColumn("Heure");
        RequestCB request = new RequestCB(conn);
        request.getSpectacle();
        ArrayList<Object[]> rows = request.getListModel();
        for (Object[] row : rows){
            this.addRow(row);
        }

    }
}
