package Interface; /**
 * Created by lial on 11/21/19.
 */
import Transaction.ConnectionManagement;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;


public class MainInterface extends JFrame implements ActionListener, WindowListener{
    private JComboBox<String> actionList;
    private Connection conn;
    private ConnectionManagement connection;
    public MainInterface() {
        super();
        this.addWindowListener(this);
        this.connection = new ConnectionManagement();
        connection.openConnection();
        this.conn = connection.getConn();
        setTitle("Circus Manager");
        setSize(400, 400);

        JPanel panel = new JPanel();
        JLabel lbl = new JLabel("Que voulez vous faire?");
        panel.add(lbl);
        String[] actions = {"", "", "", "", "", "", "",""};
        actions[0] = ("--Choisissez une option--");
        actions[1] = ("Ajouter un artiste/expert");
        actions[2] = ("Ajouter un numéro");
        actions[3] = ("Créer un jury et saisir les évaluations du jury");
        actions[4] = ("Afficher le classement des numéros par thème");
        actions[5] = ("Editer les spectacles");
        actions[6] = ("Ajouter un pseudo");
        actions[7] = ("Ajouter un artiste à un numéro ");
        this.actionList = new JComboBox<>(actions);
        panel.add(actionList);

        JPanel panelSouth = new JPanel();
        JButton btn = new JButton("OK");
        btn.addActionListener(this);
        panelSouth.add(btn);


        getContentPane().add(BorderLayout.CENTER, panel);
        getContentPane().add(BorderLayout.SOUTH, panelSouth);
    }


    public static void main(String[] args){
        JFrame manager = new MainInterface();
        manager.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
            int actionChosen = this.actionList.getSelectedIndex();
            switch (actionChosen){
                case 0:
                    JOptionPane.showMessageDialog(this, "Veuillez choisir une option");
                    break;
                case 1:
                    try{
                        new InputArtiste(conn);
                    } catch (Exception error){
                        System.err.println("Add Artiste Failed");
                        error.printStackTrace(System.err);
                    }
                    break;
                case 2:
                    try{
                        new InputNumero(conn);
                    } catch (Exception error){
                        System.err.println("Add Numero Failed");
                        error.printStackTrace(System.err);
                    }
                    break;
                case 3:
                    try{
                        new InputJurys(conn);
                    } catch (Exception error){
                        System.err.println("Add Evaluation Failed");
                        error.printStackTrace(System.err);
                    }
                    break;
                case 4:
                    try{
                        new SelectTheme(conn);
                    } catch (Exception error){
                        System.err.println("Show Select Theme Failed");
                        error.printStackTrace(System.err);
                    }
                    break;
                case 5:
                    try{
                        new SelectShow(conn);
                    } catch (Exception error){
                        System.err.println("Select Show Failed");
                        error.printStackTrace(System.err);
                    }
                    break;
                case 6:
                    try{
                        new InputPseudo(conn);
                    } catch (Exception error){
                        System.err.println("Select Show Failed");
                        error.printStackTrace(System.err);
                    }
                    break;
                case 7:
                    try{
                        new InputArtisteToNumero(conn);
                    } catch (Exception error){
                        System.err.println("Select Show Failed");
                        error.printStackTrace(System.err);
                    }
                    break;
            }
    }

    // Methods to manage window methods
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.connection.closeConnection();
        System.exit(0);
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
