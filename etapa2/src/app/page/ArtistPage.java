package app.page;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.Artist;

import java.util.ArrayList;

public class ArtistPage implements Page {
	private Artist artist;

	public ArtistPage(Artist artist) {
		this.artist = artist;
	}

	public String print() {
		StringBuilder out = new StringBuilder(new String());

		out.append("Albums:\n\t[");
		for (Album album : artist.getAlbums()) {
			out.append(album.getName());
			out.append(", ");
		}
		out.deleteCharAt(out.length() - 1); // delete last space
		out.deleteCharAt(out.length() - 1); // delete last comma

		out.append("]\n\nMerch:\n\t[");
		for (Artist.Merch merch : artist.getMerch()) {
			out.append(merch.getName());
			out.append(" - ");
			out.append(merch.getPrice());
			out.append(":\n\t");
			out.append(merch.getDescription());
			out.append(", ");
		}
		out.deleteCharAt(out.length() - 1); // delete last space
		out.deleteCharAt(out.length() - 1); // delete last comma

		out.append("]\n\nEvent:\n\t[");
		for (Artist.Event event : artist.getEvents()) {
			out.append(event.getName());
			out.append(" - ");
			out.append(event.getDate());
			out.append(":\n\t");
			out.append(event.getDescription());
			out.append(", ");
		}
		out.deleteCharAt(out.length() - 1); // delete last space
		out.deleteCharAt(out.length() - 1); // delete last comma

		out.append("]");

		return out.toString();
	}
}
