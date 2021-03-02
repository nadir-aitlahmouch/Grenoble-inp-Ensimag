/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import javax.annotation.Resource;
import javax.sql.DataSource;
import beans.Utilisateur;
import dao.UtilisateurDao;
import dao.DAOException;
import dao.PartieDao;
import beans.Partie;

//@WebServlet(name = "restriction", urlPatterns = {"/restriction"})
public class Restriction extends HttpServlet {
    @Resource(name = "jdbc/bibliography")
    private DataSource ds;
    public static final String ACCES_PUBLIC     = "/WEB-INF/connexion.jsp";
    public static final String ACCES_RESTREINT  = "/WEB-INF/comptePremierePage.jsp";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_PARTIE = "partieEnCours";
    public static final String ATT_PARTIE_C = "partieC";
    public static final String ATT_PARTICIPE = "participe";
    public static final String ATT_MAITRE = "maitrejeu";
    
    
    private void erreurBD(HttpServletRequest request,
                HttpServletResponse response, DAOException e)
            throws ServletException, IOException {
        e.printStackTrace(); // permet d’avoir le détail de l’erreur dans catalina.out
        request.setAttribute("erreurMessage", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
    }
    
    
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Récupération de la session depuis la requête */
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        
        /*
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté.
         */
        if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            /* Redirection vers la page publique */
            
            this.getServletContext().getRequestDispatcher( ACCES_PUBLIC ).forward( request, response );
        } else {
                /* Affichage de la page restreinte */
                /* verification d'une partie en cours */
                PartieDao partieDao = new PartieDao(ds);
                Partie partie =  new Partie();
                UtilisateurDao utilisateurdao = new UtilisateurDao(ds);
                try {
                        boolean reponse = partieDao.partieEnCours(partie);
                        if (reponse){
                             request.setAttribute(ATT_PARTIE,"1");
                             String Name = ((Utilisateur)session.getAttribute(ATT_SESSION_USER)).getNom();
                             boolean participe = utilisateurdao.participePartie(Name);
                             boolean maitrePartie = utilisateurdao.maitrePartie(Name);
                             if(participe){
                                 request.setAttribute(ATT_PARTICIPE, "1");
                             } else {
                                 request.setAttribute(ATT_PARTICIPE, "0");
                                 request.setAttribute("nombre", partie.getNbJoueurs());
                             }if(maitrePartie){
                                 request.setAttribute(ATT_MAITRE, "1");
                                 session.setAttribute(ATT_MAITRE, "1");
                                 System.err.println("maitre = 1 " );
                             }else{
                                 request.setAttribute(ATT_MAITRE, "0");
                                 session.setAttribute(ATT_MAITRE, "0");
                             }
                        } else {
                            request.setAttribute(ATT_PARTIE,"0");
                            request.setAttribute(ATT_PARTICIPE, "0");
                            request.setAttribute(ATT_MAITRE, "0");
                        }
                        System.err.println("participe = " + request.getAttribute(ATT_PARTICIPE));
                        request.setAttribute(ATT_PARTIE_C,partie);
                        this.getServletContext().getRequestDispatcher( ACCES_RESTREINT ).forward( request, response );
                     } catch (DAOException e) {
                          erreurBD(request,response,e);
                     }
            }
    }
}