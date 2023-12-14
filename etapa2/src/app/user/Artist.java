package app.user;

import app.audio.Collections.Album;
import app.page.ArtistPage;
import app.page.Page;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Artist extends User {
	private ArrayList<Merch> merch = new ArrayList<>();
	private ArrayList<Event> events = new ArrayList<>();
	private ArrayList<Album> albums;
	private Page currentPage;

	public Artist(User user, String name) {
		super(user.username, user.age, user.city);
		this.username = name;
		this.currentPage = new ArtistPage(this);
	}

	public void addMerch(Merch merch) {
		this.merch.add(merch);
	}

	public void addAlbum(Album album) {
		this.albums.add(album);
	}

	public void addEvent(Event event) {
		this.events.add(event);
	}

	@Getter
	static public class Merch {
		private String name;
		private String description;
		private int price;
	}

	@Getter
	static public class Event {
		private String name;
		private String date;
		private String description;
	}

}
