package app.page;

import app.audio.Collections.Album;
import app.user.Artist;
import lombok.Getter;

@Getter
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
		if (!artist.getAlbums().isEmpty()) {
			out.deleteCharAt(out.length() - 1); // delete last space
			out.deleteCharAt(out.length() - 1); // delete last comma
		}

		out.append("]\n\nMerch:\n\t[");
		for (Artist.Merch merch : artist.getMerch()) {
			out.append(merch.getName());
			out.append(" - ");
			out.append(merch.getPrice());
			out.append(":\n\t");
			out.append(merch.getDescription());
			out.append(", ");
		}
		if (!artist.getMerch().isEmpty()) {
			out.deleteCharAt(out.length() - 1); // delete last space
			out.deleteCharAt(out.length() - 1); // delete last comma
		}

		out.append("]\n\nEvents:\n\t[");
		for (Artist.Event event : artist.getEvents()) {
			out.append(event.getName());
			out.append(" - ");
			out.append(event.getDate());
			out.append(":\n\t");
			out.append(event.getDescription());
			out.append(", ");
		}
		if (!artist.getEvents().isEmpty()) {
			out.deleteCharAt(out.length() - 1); // delete last space
			out.deleteCharAt(out.length() - 1); // delete last comma
		}

		out.append("]");

		return out.toString();
	}
}
