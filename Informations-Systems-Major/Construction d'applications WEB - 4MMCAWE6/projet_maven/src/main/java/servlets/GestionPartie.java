/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Joueur;
import beans.Partie;
import beans.Proposed;
import dao.PartieDao;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import dao.ExercerPouvoirDao;
import dao.JoueurDao;
import dao.MessageDao;
import dao.DAOException;

/**
 * Ce contrôleur gère une partie en cours
 * @author Equipe 9
 */
@WebServlet(name = "GestionPartie", urlPatterns = {"/GestionPartie"})
public class GestionPartie extends HttpServlet {
    @Resource(name = "jdbc/bibliography")
    private DataSource ds;
    public static final String VUE              = "/WEB-INF/jeuMaitre.jsp";
    public static final String VUE_FIN              = "/WEB-INF/fin.jsp";
    
    
    /* pages d'erreurs */
    private void invalidParameters(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/controleurErreur.jsp").forward(request, response);
    }

    /** Page d'erreur à afficher en cas d'une erreur de base de données **/
    private void erreurBD(HttpServletRequest request,
            HttpServletResponse response, DAOException e)
            throws ServletException, IOException {
        e.printStackTrace(); // permet d’avoir le détail de l’erreur dans catalina.out
        request.setAttribute("erreurMessage", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
                String action = request.getParameter("action");
                HttpSession session = request.getSession();
                PartieDao partiedao = new PartieDao(ds);
                Partie partie = new Partie();
                JoueurDao joueurdao = new JoueurDao(ds);

                /* récuperer la liste des joueurs de la partie */
                request.setAttribute("joueurs", joueurdao.getListeJoueurs());
                /* récuperer les information de la partie */
                partiedao.partieEnCours(partie);
                /* récuparer la list des joueurs proposé */ 
                List<Proposed> proposed = partiedao.getProposed();
                request.setAttribute("proposed", proposed);
                /* récuperer le jouers qui a été tuer la période précendente */
                Joueur mort = partiedao.nouveauMort();
                request.setAttribute("mort", mort);

                if(action.equals("passernuit")){
                    partiedao.passerPeriode("Nuit", partie);
                    request.setAttribute("periode", "Nuit");
                    eliminerRactifier(request, response, partiedao, proposed, "Nuit");
                }
                
                if(action.equals("passeraujour")){
                    partiedao.passerPeriode("Jour", partie);
                    request.setAttribute("periode", "Jour");
                    eliminerRactifier(request, response, partiedao, proposed, "Jour");

                    /** vider la table de exercer pouvoir **/
                    ExercerPouvoirDao exercerPv = new ExercerPouvoirDao(ds);
                    exercerPv.videTable();

                }
                request.setAttribute("maitrejeu", "1");
                Boolean finpartie =  joueurdao.finPartie();
                if(finpartie){
                    // si la partie est terminé 
                    String gagant = joueurdao.gagnant();
                    request.setAttribute("gagnant", gagant);
                    request.setAttribute("joueurs", joueurdao.getListeJoueurs());
                    request.setAttribute("messages", (new MessageDao(ds)).getListeMessages("archive"));
                    partiedao.deletePartie();
                    this.getServletContext().getRequestDispatcher( VUE_FIN ).forward( request, response );
                }
                else{

                    response.sendRedirect("/projetAcol/Jeu");

                }
                
        } catch (DAOException e){
            erreurBD(request, response, e);
        }
    }

    private void eliminerRactifier(HttpServletRequest request, 
                         HttpServletResponse response, PartieDao partiedao,
                         List<Proposed> proposed, String periode) 
            throws ServletException, IOException {
            try {
                int max = partiedao.nbJoueurs(periode.equals("Jour")?true:false)/2;
                System.err.println(max);
                String eliminer = null;
                for (Proposed joueur:proposed){
                    System.err.println(joueur.getNbVote());
                    if (joueur.getNbVote() > max){
                        max = joueur.getNbVote();
                        eliminer = joueur.getPseudonyme();
                    }
                }
                partiedao.viderProposed();
                if (eliminer != null){

                    partiedao.changeStatut(eliminer);
                }
            } catch (DAOException e){
            erreurBD(request, response, e);
        }
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
