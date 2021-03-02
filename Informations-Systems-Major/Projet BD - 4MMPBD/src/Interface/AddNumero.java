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
import Transaction.*;

/**
 * Created by lial on 11/21/19.
 */

public class AddNumero extends JFrame implements ActionListener, WindowListener{
    private JComboBox<String> codeN;
    private int codeSpectacle;
    private HashMap<Integer, Integer> cbNumero;
    private EditShow parent;
    private Connection conn;

    public AddNumero(Connection conn, int codeSpectacle, EditShow parent){
        super();
        this.conn = conn;
        this.codeSpectacle = codeSpectacle;
        this.parent = parent;

        RequestCB request = new RequestCB(this.conn);
        setTitle("Ajouter un numéro au spectacle");
        setSize(600, 400);
        JPanel panelPrincipal = new JPanel();
        GridLayout layout = new GridLayout(0,2);
        layout.setVgap(20);
        panelPrincipal.setLayout(layout);


        JLabel lbl1 = new JLabel("Numéro à ajouter");
        panelPrincipal.add(lbl1);
        request.getNumerosThemeSpectacle(codeSpectacle);
        this.codeN = request.getCb();
        this.cbNumero = request.getMap();
        panelPrincipal.add(this.codeN);


        JPanel panelBot = new JPanel();
        JButton btn = new JButton("Envoyer");
        btn.addActionListener(this);
        panelBot.add(btn);

        JPanel panelTop = new JPanel();
        JLabel lbl = new JLabel("Choisissez le numéro à ajouter au spectacle");
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
        int codeNint = cbNumero.get(this.codeN.getSelectedIndex());
        int somme = new FetchDuree(conn).getDuree(codeNint);
        somme += new FetchTimeNumero(conn).getTotalTime(this.codeSpectacle);
        if(somme > 180){
            JOptionPane.showMessageDialog(this, "Le numéro est trop long, le spectacle ne peut pas faire plus de 180 minutes");
        }
        else{
            AjoutNumeroDansSpectacle ajout = new AjoutNumeroDansSpectacle();
            try{
                conn.setSavepoint();
                ajout.AjoutNumeroDansSpectacle(conn, codeNint, this.codeSpectacle);
                JOptionPane.showMessageDialog(this, "Ajout du numéro au spectacle réussi");
                conn.commit();
            } catch (SQLException error) {
                ConnectionManagement.rollback(conn);
                switch (error.getErrorCode()) {
                    case 1:
                        JOptionPane.showMessageDialog(this, "Le numéro est déjà présent dans ce spectacle.");
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, error.getMessage());
                        break;
                }
            }
            this.parent.refreshList();
        }


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
