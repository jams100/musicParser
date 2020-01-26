//James Redmond, c15339336

package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.ForeignKey;

@Entity
public class Playlist {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int playlistid;
	int id;
	String name;
	@ManyToMany
	List<Track> tracks=new ArrayList<>();

	//Empty Constructor
	public Playlist() {
		super();
		// TODO Auto-generated constructor stub
	}

	//Constructor With Details
	public Playlist(int id, String name, List<Track> tracks) {
		super();
		this.id = id;
		this.name = name;
		this.tracks = tracks;
	}

	//Getters & Setters
	public int getPlaylistid() {
		return playlistid;
	}

	public void setPlaylistid(int playlistid) {
		this.playlistid = playlistid;
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

	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}

	@Override
	public String toString() {
		return "Playlist [id=" + id + ", name=" + name + ", tracks=" + tracks + "]";
	}
}