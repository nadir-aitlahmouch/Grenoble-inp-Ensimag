package Interface.Model;

import Transaction.RequestCB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by lial on 11/27/19.
 */
public class ArtisteModel extends DefaultTableModel{
    public ArtisteModel(Connection conn){
        super();
        this.addColumn("Code Artiste");
        this.addColumn("Nom");
        this.addColumn("Prénom");
        this.addColumn("Sélectionné");
        RequestCB request = new RequestCB(conn);
        request.getArtistesNumero();
        ArrayList<Object[]> rows = request.getListModel();
        for (Object[] row : rows){
            Object[] newRow = {row[0], row[1], row[2], false};
            this.addRow(newRow);
        }

    }
}
