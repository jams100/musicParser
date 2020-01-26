//James Redmond, c15339336

package entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	String libraryPersistentId;
	String name;
	String password;
	@OneToMany
	List<Track> tracks;
	@OneToMany
	List<Playlist> playlists;

	//Empty Constructor
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	//Constructor With Details
	public User(String libraryPersistentId, String name, String password, List<Track> tracks,
			List<Playlist> playlists) {
		super();
		this.libraryPersistentId = libraryPersistentId;
		this.name = name;
		this.password = password;
		this.tracks = tracks;
		this.playlists = playlists;
	}

	//Getters & Setters for PersistenId etc
	public String getLibraryPersistentId() {
		return libraryPersistentId;
	}
	public void setLibraryPersistentId(String libraryPersistentId) {
		this.libraryPersistentId = libraryPersistentId;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public List<Playlist> getPlaylists() {
		return playlists;
	}
	public void setPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}

	public List<Track> getTracks() {
		return tracks;
	}
	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}
}