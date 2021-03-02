package Interface.Model;

import Transaction.RequestCB;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by lial on 11/27/19.
 */
public class NumeroModel extends DefaultTableModel{
    public NumeroModel(Connection conn, int codeSpectacle){
        super();
        this.addColumn("Code Numéro");
        this.addColumn("Titre");
        this.addColumn("Durée");
        RequestCB request = new RequestCB(conn);
        request.getNumerosSpectacle(codeSpectacle);
        ArrayList<Object[]> rows = request.getListModel();
        for (Object[] row : rows){
            this.addRow(row);
        }

    }
}
