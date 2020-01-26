package product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import dao.PlaylistDAO;
import dao.TrackDAO;
import dao.UserDAO;
import entities.Playlist;
import entities.Track;
import entities.User;
import ParseMusic.Parse;

@Path("/sampleproduct")
public class Action {
	private static List<Track> tracks;
	private static List<Playlist> playlist;
	private static User user;
	
	static {
		Parse parse = new Parse();
		try {
			parse.parse();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// CREATING ARRAYLIST OF TRACKS
		ArrayList<Track> tracksStatic = parse.getTracks();
		tracks = tracksStatic;
		
		// USING HIBERNATE TO SAVE TRACKS
		TrackDAO trackdao = new TrackDAO();
		for (Track t : tracks) {
			trackdao.saveTrack(t);
		}
		
		// CREATING ARRAYLIST OF PLAYLISTS
		ArrayList<Playlist> playlistNew = parse.getPlaylists();
		playlist=playlistNew;
		
		// USING HIBERNATE TO SAVE TRACKS
		PlaylistDAO playlistdao = new PlaylistDAO();
		for (Playlist p: playlist) {
			playlistdao.savePlaylist(p);
		}
		
		// CREATING USER AND PERSISTING
		user = new User(parse.getLibraryPersistentId(), "James Redmond", "12345", tracks, playlist);
		UserDAO userdao = new UserDAO();
		userdao.saveuser(user);
	}
	
	@GET
	@Path("/tracks")
	@Produces("text/plain")
	public String tracks() {
		return tracks.toString();
	}

	@GET
	@Path("/hello")
	@Produces("text/plain")
	public String hello() {
		return tracks.toString();
	}
	
	// GET ALL TRACKS FROM USER: STRING
		@GET
		@Path("/user/{name}/{password}/getalltracks")
		@Produces("text/plain")
		public String getAllTracksUser(@PathParam("name") String name, @PathParam("password") String password) {
			if (name.equals(user.getName()) && password.equals(user.getPassword())) {
				String greetings = "Hello user name: " + name + " password: " + password + " Library Persistent ID"
						+ user.getLibraryPersistentId() + "\n";

				ArrayList<Track> tracks = new ArrayList<>();
				for (Track t : user.getTracks()) {
					tracks.add(t);
				}

				return greetings + tracks.toString();
			}
			System.out.println("Incorrect user");
			return "Incorrect user";

		}
	
		// GET ALL TRACKS FROM USER: XML
		@GET
		@Path("/user/{name}/{password}/getalltracks/xml")
		@Produces("application/xml")
		public List<Track> getAllTracksUserXML(@PathParam("name") String name, @PathParam("password") String password) {

			if (name.equals(user.getName()) && password.equals(user.getPassword())) {
				ArrayList<Track> tracks = new ArrayList<Track>();
				for (Track t : user.getTracks()) {
					tracks.add(t);
				}

				return tracks;
			}
			System.out.println("not working.");
			return null;

		}

		// GET ALL PLAYLISTS FROM USER: STRING
		@GET
		@Path("/user/{name}/{password}/getallplaylists")
		@Produces("text/plain")
		public String getAllPlaylistsUserxml(@PathParam("name") String name, @PathParam("password") String password) {
			if (name.equals(user.getName()) && password.equals(user.getPassword())) {
				String greetings = "Hello user name: " + name + " password: " + password + " Library Persistent ID"
						+ user.getLibraryPersistentId() + "\n";

				List<Playlist> playlists = new ArrayList<>();
				playlists = user.getPlaylists();

				return greetings + " " + new ArrayList<>(playlists).toString();
			}
			System.out.println("Incorrect user");
			return "Incorrect user";

		}

		// GET ALL PLAYLISTS FROM USER: XML
		@GET
		@Path("/user/{name}/{password}/getallplaylists/xml")
		@Produces("application/xml")
		public List<Playlist> getAllPlaylistsUser(@PathParam("name") String name, @PathParam("password") String password) {
			if (name.equals(user.getName()) && password.equals(user.getPassword())) {
				//ArrayList<Playlist> playlists = new ArrayList<Playlist>();
				ArrayList<Playlist> sampleList = new ArrayList<Playlist>();
				Playlist plist = new Playlist(1, "rand", new ArrayList<Track>());
				sampleList.add(plist);
//				for (Playlist p : user.getPlaylists()) {
//					playlists.add(p);
//				}
				return sampleList;
			}
			System.out.println("Incorrect user");
			return null;
		}
		
		// GET SPECIFIC TRACK FROM USER: STRING
		@GET
		@Path("/user/{name}/{password}/gettrack/{id}")
		@Produces("text/plain")
		public String getSpecificTracksUser(@PathParam("name") String name, @PathParam("password") String password,
				@PathParam("id") String id) {
			if (name.equals(user.getName()) && password.equals(user.getPassword())) {
				String greetings = "Hello user name: " + name + " password: " + password + " Library Persistent ID"
						+ user.getLibraryPersistentId() + "\n";

				for (Track t : user.getTracks()) {
					if (t.getId() == Integer.parseInt(id)) {
						return greetings + t.toString();
					}
				}

				return greetings + "track not found";
			}
			System.out.println("Incorrect user");
			return "Incorrect user";

		}

		// GET SPECIFIC PLAYLISTS FROM USER: STRING
		@GET
		@Path("/user/{name}/{password}/getplaylist/{id}")
		@Produces("text/plain")
		public String getSpecificPlaylistUser(@PathParam("name") String name, @PathParam("password") String password,
				@PathParam("id") String id) {
			if (name.equals(user.getName()) && password.equals(user.getPassword())) {
				String greetings = "Hello user name: " + name + " password: " + password + " Library Persistent ID"
						+ user.getLibraryPersistentId() + "\n";

				for (Playlist p : user.getPlaylists()) {
					if (p.getId() == Integer.parseInt(id)) {
						return greetings + p.toString() + "\n" + p.getTracks();
					}
				}

				return greetings + "track not found";
			}
			System.out.println("Incorrect user");
			return "Incorrect user";

		}
		
		// ADD TRACK FOR USER
		@POST
		@Path("/user/{name}/{password}/tracks/addxml")
		@Consumes("application/xml")
		@Produces("application/xml")

		public Track addTrackXML(@PathParam("name") String name, @PathParam("password") String password, Track track) {
			if (name.equals(user.getName()) && password.equals(user.getPassword())) {
				TrackDAO trackdao = new TrackDAO();
				user.getTracks().add(track);
				trackdao.saveTrack(track);
				return track;
			}
			System.out.println("Did not go through");
			return track;
		}

		// UPDATE TRACK FOR USER
		@POST
		@Path("/user/{name}/{password}/tracks/updatexml")
		@Consumes("application/xml")
		@Produces("application/xml")

		public Track updateTrackXML(@PathParam("name") String name, @PathParam("password") String password, Track track) {
			if (name.equals(user.getName()) && password.equals(user.getPassword())) {
				TrackDAO trackdao = new TrackDAO();
				int count = 0;
				for (Track t : user.getTracks()) {
					if (t.getId() == track.getId()) {
						user.getTracks().set(count, track);
						trackdao.updateTrack(track);
					}
					count++;
				}
				return track;
			}
			System.out.println("did not go through");
			return track;
		}

		// DELETE TRACK FOR USER
		@DELETE
		@Path("/user/{name}/{password}/tracks/deletexml/{id}")
		@Produces("text/plain")

		public String deleteTrackXML(@PathParam("name") String name, @PathParam("password") String password,
				@PathParam("id") String id) {
			if (name.equals(user.getName()) && password.equals(user.getPassword())) {
				TrackDAO trackdao = new TrackDAO();
				for (int i = 0; i < user.getTracks().size(); i++) {
					if (user.getTracks().get(i).getId() == Integer.parseInt(id)) {
						trackdao.deleteTrack(user.getTracks().get(i).getId());
						user.getTracks().remove(i);
						System.out.println("Deleted track" + i);
					}
				}
				return "deleted";
			}
			System.out.println("did not go through");
			return "deleted";
		}
		
}