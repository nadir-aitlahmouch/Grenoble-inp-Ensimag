package Interface;

import Interface.InputNumero;
import Interface.Model.ArtisteModel;
import Interface.Tables.ArtisteJTable;
import Transaction.RequestCB;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by lial on 11/28/19.
 */
public class AddArtiste extends JFrame implements ActionListener, WindowListener {
    private InputNumero parent;
    private Connection conn;
    private DefaultTableModel model;
    private ArtisteJTable table;
    public AddArtiste(Connection conn, InputNumero parent){
        this.parent = parent;
        this.conn = conn;
        RequestCB request = new RequestCB(conn);
        setTitle("Sélectionner des artistes");
        setSize(600, 400);
        JPanel panelPrincipal = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panelPrincipal.setLayout(layout);
        c.fill = GridBagConstraints.BOTH;


        JLabel lbl0 = new JLabel("Choisissez le ou les artistes");

        lbl0.setFont(lbl0.getFont().deriveFont(lbl0.getFont().getStyle() | Font.BOLD));

        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.weightx = 0.30;
        panelPrincipal.add(lbl0, c);


        // Création de table
        this.model = new ArtisteModel(conn);
        this.table = new Interface.Tables.ArtisteJTable(this.model);
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
        // Envoie un set d'artiste au parent (InputNumero) afin qu'il puisse envoyer lui même les relations
        this.parent.setRowsSelected(new HashSet<>());
        for(int i=0; i<this.table.getRowCount(); i++){
            if((boolean)this.table.getValueAt(i, 3)){
                Object[] row = {this.table.getValueAt(i,0), this.table.getValueAt(i,1), this.table.getValueAt(i,2)};
                this.parent.getRowsSelected().add(row);
            }
        }
        this.parent.refresh();
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
