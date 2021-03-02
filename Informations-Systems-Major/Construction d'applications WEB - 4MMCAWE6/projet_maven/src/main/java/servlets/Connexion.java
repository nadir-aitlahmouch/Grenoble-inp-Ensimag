/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import beans.Utilisateur;
import dao.UtilisateurDao;
import forms.ConnexionForm;
import dao.DAOException;




/**
 * Controleur de la connexion dans l'application
 * @author Equipe 9
 */

@WebServlet(name = "connexion", urlPatterns = {"/connexion"})
public class Connexion extends HttpServlet {
    
    @Resource(name = "jdbc/bibliography")
    private DataSource ds;
    public static final String ATT_USER         = "utilisateur";
    public static final String ATT_FORM         = "form";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String VUE              = "/WEB-INF/connexion.jsp";
    
    /**
     * Page d'erreur
     */
    private void erreurBD(HttpServletRequest request,
                HttpServletResponse response, DAOException e)
            throws ServletException, IOException {
        e.printStackTrace(); // permet d’avoir le détail de l’erreur dans catalina.out
        request.setAttribute("erreurMessage", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
    }
    
  
    /**
     * Handles the HTTP <code>GET</code> method.
     *  Redirection vers la page d'inscription ou vérification de l'expiration de la session.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Affichage de la page de connexion */
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
                if (action == null){ 
                 response.sendRedirect("/projetAcol/restriction");
                } else if (action.equals("inscription")){
                    // redirection vers la page d'inscription
                    this.getServletContext().getRequestDispatcher("/WEB-INF/inscription.jsp").forward(request,response);
            }
            
        } catch (DAOException e){
            erreurBD(request, response, e);
        }
        
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
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Préparation de l'objet formulaire */
        ConnexionForm form = new ConnexionForm();
        UtilisateurDao userDao = new UtilisateurDao(ds);

        /* Traitement de la requête et récupération du bean en résultant */
        Utilisateur utilisateur = form.connecterUtilisateur( request , userDao);

        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();
        
        /**
         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
         * Utilisateur à la session, sinon suppression du bean de la session.
         */
        
        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, utilisateur );
        
        
        if ( form.getErreurs().isEmpty() ) {
            /* Si le formulaire de l'inscription est bien rempli */
            session.setAttribute( ATT_SESSION_USER, utilisateur );
            response.sendRedirect("/projetAcol/restriction");
        } else {
            /* Le formulaire est mal rempli */
            session.setAttribute( ATT_SESSION_USER, null );
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }
    }
}
