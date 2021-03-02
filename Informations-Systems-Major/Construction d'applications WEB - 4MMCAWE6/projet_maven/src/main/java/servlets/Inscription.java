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
import forms.InscriptionForm;
import beans.Utilisateur;
import dao.UtilisateurDao;
import dao.DAOException;

/**
 * contrôleur de l'inscription d'un utilisateur
 * @author Equipe 9
 */
@WebServlet(name = "Inscription", urlPatterns = {"/inscription"})
public class Inscription extends HttpServlet {
    
    @Resource(name = "jdbc/bibliography")
    private DataSource ds;
    public static final String ATT_USER = "utilisateur";
    public static final String ATT_FORM = "form";
    public static final String VUE = "/WEB-INF/inscription.jsp";
	
    /* pages d'erreurs */
    private void invalidParameters(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/controleurErreur.jsp").forward(request, response);        
    }
    
    private void erreurBD(HttpServletRequest request,
                HttpServletResponse response, DAOException e)
            throws ServletException, IOException {
        e.printStackTrace(); // permet d’avoir le détail de l’erreur dans catalina.out
        request.setAttribute("erreurMessage", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
    }
    
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
        
        
        /* Affichage de la page d'inscription */
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
	
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
        
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try{
            if (action == null){
                 this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
            } else if (action.equals("connexion")){
                
                this.getServletContext().getRequestDispatcher("/WEB-INF/connexion.jsp").forward(request,response);
            } else if (action.equals("inscription")){
                /* Préparation de l'objet formulaire */
                InscriptionForm form = new InscriptionForm();
                UtilisateurDao userDao = new UtilisateurDao(ds);
                /* Appel au traitement et à la validation de la requête, et récupération du bean en résultant */
                Utilisateur utilisateur = form.inscrireUtilisateur(request, userDao);
                /* Stockage du formulaire et du bean dans l'objet request */
                request.setAttribute( ATT_FORM, form );
                request.setAttribute( ATT_USER, utilisateur );

                this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );    
            }
        } catch (DAOException e){
            erreurBD(request, response, e);
        }
        
    }
}
