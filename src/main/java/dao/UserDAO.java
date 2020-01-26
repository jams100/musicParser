//James Redmond, c15339336

package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import entities.Track;
import entities.User;

public class UserDAO {
	Configuration config;
	SessionFactory sFactory;
	public UserDAO() {
		this.config = new Configuration();
		config.configure("hibernate.cfg.xml");
		this.sFactory=HibernateUtil.getSessionFactory();
	}
	
	public void saveuser(User user) {
		try {
			Session sObj = sFactory.openSession();
			Transaction transactionObj = sObj.beginTransaction();
			sObj.save(user);
			transactionObj.commit();
			sObj.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	public List<Track> getTracks(){
		Session session=sFactory.openSession();
		Query query = session.createQuery("from Track");
		List<Track> tracks = query.list();
		session.close();
		return tracks;		
	}
	
	public int deleteTrack() {
		return 0;
		
	}
	
	public int updateDepartment(Track track) {
		if (track.getId() <= 0)
			return 0;
		Session session=sFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "Update Track set deptname = :name where id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", track.getId());
		query.setParameter("name", track.getId());
		int rowCount = query.executeUpdate();

		System.out.println("Rows affected: " + rowCount);
		tx.commit();
		session.close();
		return rowCount;
	}
	

}