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

public class SelectTheme extends JFrame implements ActionListener, WindowListener{
    private JComboBox<String> Theme;
    private HashMap<Integer, String> cbTheme;
    private Connection conn;

    public SelectTheme(Connection conn) throws SQLException{
        super();
        this.conn = conn;
        RequestCB request = new RequestCB(conn);
        setTitle("Thème");
        setSize(220, 200);
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel();


        JLabel lbl0 = new JLabel("Thème");
        panelPrincipal.add(lbl0);
        request.getThemes(false);
        this.Theme = request.getCb();
        this.cbTheme = request.getMapString();
        panelPrincipal.add(this.Theme);



        JPanel panelBot = new JPanel();
        JButton btn = new JButton("Confirmer");
        btn.addActionListener(this);
        panelBot.add(btn);

        JPanel panelTop = new JPanel();
        JLabel lbl = new JLabel("Sélectionner le thème");
        panelTop.add(lbl);

        this.add(container);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panelPrincipal.setBorder(padding);
        container.add(BorderLayout.NORTH, panelTop);
        container.add(BorderLayout.CENTER, panelPrincipal);
        container.add(BorderLayout.SOUTH, panelBot);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event){
        try {
            DisplayLeaderboard window = new DisplayLeaderboard(conn, (String) this.Theme.getSelectedItem());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
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
