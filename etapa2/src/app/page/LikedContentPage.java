package app.page;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.ArrayList;

public class LikedContentPage implements Page {
	private final User user;

	public LikedContentPage(User user) {
		this.user = user;
	}

	public String print() {
		StringBuilder out = new StringBuilder(new String());

		out.append("Liked songs:\n\t[");
		for (Song song : user.getLikedSongs()) {
			out.append(song.getName()).append(" - ").append(song.getArtist());
			out.append(", ");
		}
		out.deleteCharAt(out.length() - 1); // delete last comma
		out.append("]\n\nFollowed playlists:\n\t[");
		for (Playlist playlist : user.getFollowedPlaylists()) {
			out.append(playlist.getName()).append(" - ").append(playlist.getOwner());
			out.append(", ");
		}
		out.deleteCharAt(out.length() - 1); // delete last comma
		out.append("]");

		return out.toString();
	}
}
