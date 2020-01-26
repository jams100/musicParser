//James Redmond, c15339336

package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import entities.Playlist;
import entities.Track;

public class PlaylistDAO {
	Configuration config;
	SessionFactory sFactory;
	public PlaylistDAO() {
		this.config = new Configuration();
		config.configure("hibernate.cfg.xml");
		this.sFactory=HibernateUtil.getSessionFactory();
	}
	
	public void savePlaylist(Playlist playlist) {
		try {
			Session sObj = sFactory.openSession();
			Transaction transactionObj = sObj.beginTransaction();
			sObj.save(playlist);
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
	
	public int updatePlaylist(Playlist playlist) {
		if (playlist.getId() <= 0)
			return 0;
		Session session=sFactory.openSession();
		Transaction tx = session.beginTransaction();
		String hql = "Update Track set deptname = :name where id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", playlist.getId());
		query.setParameter("name", playlist.getId());
		int rowCount = query.executeUpdate();

		System.out.println("Rows affected: " + rowCount);
		tx.commit();
		session.close();
		return rowCount;
	}
	
	public int deletePlaylist() {
		return 0;
	
	}
}