package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.page.ArtistPage;
import app.player.PlayerStats;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Artist extends User {
	private final ArrayList<Merch> merch = new ArrayList<>();
	private final ArrayList<Event> events = new ArrayList<>();
	private final ArrayList<Album> albums = new ArrayList<>();

	public Artist(String username, int age, String city) {
		super(username, age, city);
		online = false;
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

	public boolean hasAlbumByName(final String albumName) {
		for (Album album : albums)
			if (album.getName().equalsIgnoreCase(albumName))
				return true;

		return false;
	}

	/**
	 * It is NOT safe to delete the artist if:<br>
	 *  - A user's player is running and the current song is of the artist's<br>
	 *  - A user's selected page is the artist's
	 *
	 * @return true / false
	 */
	@Override
	public boolean isSafeToDelete() {
		boolean safeDelete = true;

		for (User user : Admin.getUsers()) {
			if (user.getUsername().equalsIgnoreCase(username) || user.player.getCurrentAudioFile() == null)
				continue;

			PlayerStats playerStats = user.getPlayerStats();
			if (!playerStats.isPaused() && this.createdSongByName(user.player.getCurrentAudioFile().getName())) {
				safeDelete = false;
				break;
			}

			if (user.getCurrentPage() instanceof ArtistPage artistPage && artistPage.getArtist() == this) {
				safeDelete = false;
				break;
			}
		}

		return safeDelete;
	}

	public boolean createdSongByName(final String name) {
		for (Album album : albums)
			for (Song song : album.getSongs())
				if (song.getName().equalsIgnoreCase(name))
					return true;
		return false;
	}

	public boolean hasEventByName(String eventName) {
		for (Event event : events)
			if (event.getName().equalsIgnoreCase(eventName))
				return true;
		return false;
	}

	public boolean hasMerchByName(final String merchName) {
		for (Merch merchEntry : merch)
			if (merchEntry.getName().equalsIgnoreCase(merchName))
				return true;
		return false;
	}

	public Album getAlbumByName(String albumName) {
		for (Album album : albums)
			if (album.getName().equals(albumName))
				return album;
		return null;
	}

	@Getter
	static public class Merch {
		public Merch(String name, String description, int price) {
			this.name = name;
			this.description = description;
			this.price = price;
		}

		private String name;
		private String description;
		private int price;
	}

	@Getter
	static public class Event {
		public Event(String name, String date, String description) {
			this.name = name;
			this.date = date;
			this.description = description;
		}

		private String name;
		private String date;
		private String description;
	}

}
