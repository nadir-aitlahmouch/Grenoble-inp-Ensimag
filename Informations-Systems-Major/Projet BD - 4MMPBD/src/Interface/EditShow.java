package Interface;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import Interface.Model.ArtisteModel;
import Interface.Model.NumeroModel;
import Interface.Tables.NumeroJTable;
import Transaction.*;

/**
 * Created by lial on 11/21/19.
 */

public class EditShow extends JFrame implements ActionListener, WindowListener{
    private NumeroJTable table;
    private NumeroModel model;
    private JButton addNumero;
    private JButton delNumero;
    private JPanel panelPrincipal;
    private JLabel dureeTotal;
    private JLabel lbl1; // Va contenir le texte "Durée Totale"
    private int codeSpectacle;
    private Connection conn;

    public EditShow(Connection conn, int codeSpectacle){
        super();
        this.conn = conn;
        setTitle("Edition Spectacle");
        setSize(600, 400);
        this.codeSpectacle = codeSpectacle;
        RequestCB requestCB = new RequestCB(conn);



        panelPrincipal = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panelPrincipal.setLayout(layout);
        c.fill = GridBagConstraints.BOTH;

        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.weightx = 0.30;
        JLabel lbl0 = new JLabel("Numéros");
        panelPrincipal.add(lbl0, c);

        this.model = new NumeroModel(conn, this.codeSpectacle);
        this.table = new NumeroJTable(this.model);
        this.table.setPreferredScrollableViewportSize(this.table.getPreferredSize());
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(this.table);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.weighty = 1;
        panelPrincipal.add(scroll, c);


        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        int duree = new FetchTimeNumero(conn).getTotalTime(this.codeSpectacle);
        this.lbl1 = new JLabel("Durée Totale: " + duree + " minutes");
        panelPrincipal.add(this.lbl1, c);


        JPanel panelBot = new JPanel();
        addNumero = new JButton("Ajouter un numéro");
        addNumero.addActionListener(this);
        panelBot.add(this.addNumero);
        
        delNumero = new JButton("Supprimer ce numéro");
        delNumero.addActionListener(this);
        panelBot.add(this.delNumero);

        JPanel panelTop = new JPanel();
        JLabel lbl = new JLabel("Que voulez vous faire?");
        panelTop.add(lbl);

        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panelPrincipal.setBorder(padding);
        getContentPane().add(BorderLayout.NORTH, panelTop);
        getContentPane().add(BorderLayout.CENTER, panelPrincipal);
        getContentPane().add(BorderLayout.SOUTH, panelBot);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == addNumero){
            new AddNumero(conn, this.codeSpectacle, this);
        }
        else{
            int input = JOptionPane.showConfirmDialog(this, "Êtes vous sûr de supprimer ce numéro du spectacle?", "Attention",JOptionPane.YES_NO_OPTION);
            if (input == 0){
                // L'utilisateur a confirmé
                int codeN = (int) this.table.getValueAt(this.table.getSelectedRow(), 0);
                try {
                    conn.setSavepoint();
                    new DeleteNumero().deleteNumero(conn, codeN, this.codeSpectacle);
                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Le numéro est supprimé");
                } catch(Exception error){
                    ConnectionManagement.rollback(conn);
                    error.printStackTrace(System.err);
                    JOptionPane.showMessageDialog(this, error.getMessage());
                }
                this.refreshList();

            }


        }

    }

    // Permet de mettre à jour la table
    public void refreshList(){
        this.table.setModel(new NumeroModel(this.conn, this.codeSpectacle));
        this.panelPrincipal.revalidate();
        this.panelPrincipal.repaint();

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
