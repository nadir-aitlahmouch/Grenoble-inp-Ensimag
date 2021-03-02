package Interface;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import Transaction.*;

/**
 * Created by lial on 11/21/19.
 */

public class AddSpecialite extends JFrame implements ActionListener, WindowListener{
    private Interface.Tables.SpecialiteJTable table;
    private InputArtiste parent;
    private DefaultTableModel model;

    public AddSpecialite(Connection conn, InputArtiste parent){
        this.parent = parent;
        RequestCB request = new RequestCB(conn);
        setTitle("Sélectionner des spécialités");
        setSize(600, 400);
        JPanel panelPrincipal = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panelPrincipal.setLayout(layout);
        c.fill = GridBagConstraints.BOTH;


        JLabel lbl0 = new JLabel("Choisissez le ou les spécialités");

        lbl0.setFont(lbl0.getFont().deriveFont(lbl0.getFont().getStyle() | Font.BOLD));

        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.weightx = 0.30;
        panelPrincipal.add(lbl0, c);


        // Création de table
        Object[] columnNames = {"Spécialité", "Sélectionné"};
        this.model = new DefaultTableModel(new Object[][]{}, columnNames);
        request.getThemes(true);
        ArrayList<Object[]> listTheme = request.getListModel();
        for(Object[] specialite : listTheme){
            // Specialité est un tableau à 1 élément
            Object[] row = {specialite[0], false};
            this.model.addRow(row);
        }

        this.table = new Interface.Tables.SpecialiteJTable(this.model);
        this.table.setPreferredScrollableViewportSize(this.table.getPreferredSize());
        JScrollPane scroll = new JScrollPane(this.table);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.weighty = 1;
        panelPrincipal.add(scroll, c);






        // Bottom
        JPanel panelBot = new JPanel();
        JButton btn = new JButton("Envoyer");
        btn.addActionListener(this);
        panelBot.add(btn);


        // Top
        JPanel panelTop = new JPanel();
        JPanel titleContainer = new JPanel();
        panelTop.add(titleContainer);
        JLabel lbl = new JLabel("Veuillez rentrer les informations");
        titleContainer.add(lbl);

        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panelPrincipal.setBorder(padding);
        getContentPane().add(BorderLayout.NORTH, panelTop);
        getContentPane().add(BorderLayout.CENTER, panelPrincipal);
        getContentPane().add(BorderLayout.SOUTH, panelBot);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){

        for(int i=0; i<this.table.getRowCount(); i++){
            if((boolean)this.table.getValueAt(i, 1)){
                this.parent.getSpecialite().add((String)this.table.getValueAt(i,0));
            }
        }
        setVisible(false);
        dispose();
    }

    // Methods to manage window methods
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        setVisible(false);
        dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
