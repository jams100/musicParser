//James Redmond, c15339336

package dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Singleton Hibernate Utility class with a method to get Session Factory.
public class HibernateUtil
{
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory()
    {
        if (sessionFactory == null)
        {
            try
            {
                sessionFactory = new Configuration().configure().buildSessionFactory();
                
               // SessionFactory sessionFactory = new Configuration()
                //	    .configure("/org/nitish/caller/hibernate.cfg.xml").buildSessionFactory();
           
            } catch (Throwable ex)
            {
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        //sessionFactory.close();
        return sessionFactory;
    }
}