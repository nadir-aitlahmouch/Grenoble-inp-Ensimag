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

import Interface.Model.SpectacleModel;
import Interface.Tables.SpectacleJTable;
import Transaction.*;

/**
 * Created by lial on 11/21/19.
 */

public class SelectShow extends JFrame implements ActionListener, WindowListener{
    private JTable spectacle;
    private JButton edit;
    private JButton add;
    private JButton delete;
    private JPanel panelPrincipal;
    private Connection conn;

    public SelectShow(Connection conn) throws SQLException{
        super();
        this.conn = conn;
        RequestCB request = new RequestCB(conn);
        setTitle("Sélection Spectacle");
        setSize(600, 400);
        this.panelPrincipal = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panelPrincipal.setLayout(layout);
        c.fill = GridBagConstraints.BOTH;


        JLabel lbl0 = new JLabel("Spectacle");
        panelPrincipal.add(lbl0);
        this.spectacle = new SpectacleJTable();
        this.spectacle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.spectacle.setModel(new SpectacleModel(conn));
        panelPrincipal.add(this.spectacle, c);

        this.spectacle.setPreferredScrollableViewportSize(this.spectacle.getPreferredSize());
        JScrollPane scroll = new JScrollPane(this.spectacle);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.weighty = 1;
        panelPrincipal.add(scroll, c);




        JPanel panelBot = new JPanel();
        edit = new JButton("Editer");
        edit.addActionListener(this);
        panelBot.add(edit);

        add = new JButton("Ajouter");
        add.addActionListener(this);
        panelBot.add(add);

        delete = new JButton("Supprimer");
        delete.addActionListener(this);
        panelBot.add(delete);

        JPanel panelTop = new JPanel();
        JLabel lbl = new JLabel("Gérer les spectacles");
        panelTop.add(lbl);

        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panelPrincipal.setBorder(padding);
        getContentPane().add(BorderLayout.NORTH, panelTop);
        getContentPane().add(BorderLayout.CENTER, this.panelPrincipal);
        getContentPane().add(BorderLayout.SOUTH, panelBot);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == edit){
            int selectedRow = this.spectacle.getSelectedRow();
            if (selectedRow != -1)
                new EditShow(conn, (int)this.spectacle.getValueAt(selectedRow, 0));
        }
        else if(e.getSource() == add){
            new InputShow(this.conn, this);
        }
        else{ // Suppression d'un spectacle
            if(this.spectacle.getSelectedRow() == -1){
                JOptionPane.showMessageDialog(this, "Veuillez choisir un spectacle à supprimer");
            }else{
                try{
                    conn.setSavepoint();
                    new DeleteSpectacle().deleteSpectacle(conn, (int)this.spectacle.getValueAt(this.spectacle.getSelectedRow(), 0));
                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Le spectacle a bien été supprimé");
                    refresh();



                } catch (Exception error){
                    ConnectionManagement.rollback(conn);
                    error.printStackTrace(System.err);
                    JOptionPane.showMessageDialog(this, "Suppresion échouée");
                }
            }

        }


    }

    public void refresh(){
        this.spectacle.setModel(new SpectacleModel(conn));
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
