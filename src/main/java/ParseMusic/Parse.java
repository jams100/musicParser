//James Redmond, c15339336

package ParseMusic;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import entities.Playlist;
import entities.Track;
import entities.User;

public class Parse {

	private ArrayList<Track> tracks;
	private ArrayList<Playlist> playlists;
	User user;
	String libraryPerId;

	public void parse() throws ParserConfigurationException, SAXException, IOException 
	{
		tracks = new ArrayList();
		playlists = new ArrayList();

		//XML File to parse, Only Parsing one file
		File xmlFile = new File("C:\\Users\\James Redmond\\Documents\\TU Dublin\\Distributed Systems\\Musicxmlfiles\\iTunesMusicLibrary1.xml");

		DocumentBuilder docBuid = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
		Document document = docBuid.parse(xmlFile); 
		document.getDocumentElement().normalize();

		//Parsing XML File
		this.libraryPerId = parseHeaderItems(document);
		tracks = parseTracks(document, tracks);
		playlists = parsePlaylist(document, tracks, playlists);

		this.user = new User(libraryPerId, "James Redmond", "12345", tracks, playlists);
	}

	//** Printing Out Library Persistent Id **//
	public String parseHeaderItems(Document document) {

		NodeList dicts = document.getElementsByTagName("dict");

		NodeList dictsKeys = dicts.item(0).getChildNodes();
		for (int i = 0; i < dictsKeys.getLength(); i++) {
			if (dictsKeys.item(i).getNodeName().equals("key")) {
				if (dictsKeys.item(i).getTextContent().equals("Library Persistent ID")) {
					return (dictsKeys.item(i).getNextSibling().getTextContent());
				}
			}
		}
		return "";
	}

	//** Getting Tracks **//
	public ArrayList<Track> parseTracks(Document document, ArrayList<Track> allTrack) {

		String n=null;
		String artist=null;
		String genre=null;
		String album=null;		
		int time=0;
		int id=0;

		// Getting all dict nodes
		NodeList dicts = document.getElementsByTagName("dict");
		// Getting first dict node
		Node libraryNode = dicts.item(0);
		// Getting children of first dict element in this case first child is Major version
		NodeList libChild = libraryNode.getChildNodes();

		int total = 0;
		Node dictTracks = null;
		boolean lastDictFound = false;

		do {
			if (libChild.item(total).getNodeName().equalsIgnoreCase("dict")) {
				//Contains all of the tracks
				dictTracks = libChild.item(total);
				lastDictFound = true;
			}
			total++;
		} while (!lastDictFound);

		//Now trying to find all the keys of dictTracks children down to last key
		NodeList dictTracksChildren = dictTracks.getChildNodes();

		NodeList firstTrack = null;
		lastDictFound = false;
		allTrack = new ArrayList<>();
		total = 0;
		
		do {
			// if dictTracksChildren value is key
			if (dictTracksChildren.item(total).getNodeName().equalsIgnoreCase("key")) {

				//Last Key of dictTrackschildren
				if (dictTracksChildren.item(total).getTextContent().equals("31918") || dictTracksChildren.item(total).getTextContent().equals("4378")) {															
					lastDictFound = true;
				}
				//Else if value is not the last value then get...
			} else if (dictTracksChildren.item(total).getNodeName().equals("dict")) {

				firstTrack = dictTracksChildren.item(total).getChildNodes();

				for (int i = 0; i < firstTrack.getLength(); i++) {
					if (firstTrack.item(i).getTextContent().equals("Track ID")) {
						id = Integer.parseInt(firstTrack.item(i).getNextSibling().getTextContent());
					}
					if (firstTrack.item(i).getTextContent().equals("Name")) {
						n = firstTrack.item(i).getNextSibling().getTextContent();
					}
					if (firstTrack.item(i).getTextContent().equals("Artist")) {
						artist = firstTrack.item(i).getNextSibling().getTextContent();
					}
					if (firstTrack.item(i).getTextContent().equals("Album")) {
						album = firstTrack.item(i).getNextSibling().getTextContent();
					}
					if (firstTrack.item(i).getTextContent().equals("Genre")) {
						genre = firstTrack.item(i).getNextSibling().getTextContent();
					}
					if (firstTrack.item(i).getTextContent().equals("Total Time")) {
						time = Integer.parseInt(firstTrack.item(i).getNextSibling().getTextContent());
					}
				}
				Track track = new Track(id, n, artist, album, genre, time);
				allTrack.add(track);
			}
			total++;
		} while (!lastDictFound);

		return allTrack;
	}

	public ArrayList<Playlist> parsePlaylist(Document document, ArrayList<Track> tracks, ArrayList<Playlist> playlists) {
		playlists = new ArrayList<>();
		boolean FoundFinalArray = false;
		Node dictArray = null;// parent array from the parent dict
		NodeList playlist = null;// dict that contains the playlist information including the keys and the array with the tracks
		int trackid;// This is the track id for a playlist
		NodeList arrayTracks = null;// Contains all of the dict nodes that have the track IDs inside
		NodeList arrayTracksChildren;// Variable will hold every element of a dict node that has a track id
		String playlistName = null;
		int total = 0;
		int playlistID = 0;
		
		NodeList dicts = document.getElementsByTagName("dict");
		Node firstDictNode = dicts.item(0);
		NodeList firstDictNodeChildren = firstDictNode.getChildNodes();

		do {
			if (firstDictNodeChildren.item(total).getNodeName().equalsIgnoreCase("array")) {
				// Here we are getting the array node from the parent dict
				dictArray = firstDictNodeChildren.item(total);
				FoundFinalArray = true;
			}
			total++;
		} while (!FoundFinalArray);

		NodeList parentArrayChildren = dictArray.getChildNodes();//Each dict node has one playlist.

		for (int i = 0; i < parentArrayChildren.getLength(); i++) {
			ArrayList<Track> AllplaylistTracks = new ArrayList<>();
			if (parentArrayChildren.item(i).getNodeName().equals("dict")) {
				// Playlist that includes all tracks
				playlist = parentArrayChildren.item(i).getChildNodes();
				// Loop for iterating through playlist
				for (int m = 0; m < playlist.getLength(); m++) {
					// Here we are finding the name and playlist id through the keys
					if (playlist.item(m).getTextContent().equals("Name")) {
						playlistName = playlist.item(m).getNextSibling().getTextContent();
					} else if (playlist.item(m).getTextContent().equals("Playlist ID")) {
						playlistID = Integer.parseInt(playlist.item(m).getNextSibling().getTextContent());
					}
					//Else we are gettin the array with all the tracks
					else if (playlist.item(m).getNodeName().equals("array")) {
						arrayTracks = playlist.item(m).getChildNodes();

						// We are iterating through child dicts of the child array
						for (int p = 0; p < arrayTracks.getLength(); p++) {
							//In here we know that every odd number in the for loop has a child. Every odd number is a dict element which contains trackId & its Integer
							if (!((p % 2) == 0)) {
								
								arrayTracksChildren = arrayTracks.item(p).getChildNodes();
								trackid = Integer.parseInt(arrayTracksChildren.item(2).getTextContent());
								
								for (Track t : tracks) {
									if (t.getId() == trackid) {
										AllplaylistTracks.add(t);
									}
								}
							}
						}
					}
				}

				Playlist playlistObject = new Playlist(playlistID, playlistName, AllplaylistTracks);
				playlists.add(playlistObject);
			}
		}
		return playlists;
	}

	//Getter and Setter for Playlist
	public ArrayList<Playlist> getPlaylists() {
		return playlists;
	}
	public void setPlaylists(ArrayList<Playlist> playlists) {
		this.playlists = playlists;
	}

	//Getter & Setter for Track
	public ArrayList<Track> getTracks() {
		return tracks;
	}
	public void setTracks(ArrayList<Track> tracks) {
		this.tracks = tracks;
	}
	
	public String getLibraryPersistentId() {
		return libraryPerId;
	}
	public void setLibraryPersistentId(String libraryPersistentId) {
		this.libraryPerId = libraryPersistentId;
	}
}