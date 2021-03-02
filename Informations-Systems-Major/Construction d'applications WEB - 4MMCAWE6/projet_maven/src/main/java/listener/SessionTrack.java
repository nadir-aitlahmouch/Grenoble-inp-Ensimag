/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
/**
 *
 * @author Equipe 9
 */
public class SessionTrack implements HttpSessionListener  
  {  
     
    private static int numberOfUsersOnline;
 
    public SessionTrack() {
        numberOfUsersOnline = 0;
    }

    public static int getNumberOfUsersOnline() { 
        return numberOfUsersOnline;
    }
    
    public static void decreaseNumberOfUsersOnline() { 
        numberOfUsersOnline--;
    }
    public void sessionCreated(HttpSessionEvent event) {

        System.err.println("Session created by Id : " + event.getSession().getId());
        synchronized (this) {
         numberOfUsersOnline++;
     }

       }

       public void sessionDestroyed(HttpSessionEvent event) {

        System.err.println("Session destroyed by Id : " + event.getSession().getId());
        synchronized (this) {
        numberOfUsersOnline--;
     }

       }
   }  

