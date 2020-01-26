//James Redmond, c15339336

package dao;

import java.util.List;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import entities.Track;


public class TrackDAO {
	Configuration config;
	SessionFactory sFactory;
	public TrackDAO() {
		this.config = new Configuration();
		config.configure("hibernate.cfg.xml");
		this.sFactory=HibernateUtil.getSessionFactory();
	}
	
	public void saveTrack(Track track) {
		try {
			Session sObj = sFactory.openSession();
			Transaction transactionObj = sObj.beginTransaction();
			sObj.save(track);
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
	
	public int updateTrack(Track track) {
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
	
	public int deleteTrack(int id) {
		Session session= sFactory.openSession();
		Transaction transaction=session.beginTransaction();
		//FIX THIS
		String hql="delete from Track where id= :id";
		Query query=session.createQuery(hql);
		query.setParameter("id", id);
		int rowCount=query.executeUpdate();
		System.out.println("Rows affected: " +rowCount);
		transaction.commit();
		session.close();
		return rowCount;
		
	}
}