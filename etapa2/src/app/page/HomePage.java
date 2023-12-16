package app.page;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;
import lombok.Getter;

import java.util.ArrayList;

public class HomePage implements Page {
	private final User user;

	public HomePage(User user) {
		this.user = user;
	}

	public String print() {
		StringBuilder out = new StringBuilder(new String());

		out.append("Liked songs:\n\t[");
		for (Song song : user.getLikedSongs()) {
			out.append(song.getName());
			out.append(", ");
		}
		if (!user.getLikedSongs().isEmpty()) {
			out.deleteCharAt(out.length() - 1); // delete last space
			out.deleteCharAt(out.length() - 1); // delete last comma
		}

		out.append("]\n\nFollowed playlists:\n\t[");
		for (Playlist playlist : user.getFollowedPlaylists()) {
			out.append(playlist.getName());
			out.append(", ");
		}
		if (!user.getFollowedPlaylists().isEmpty()) {
			out.deleteCharAt(out.length() - 1); // delete last space
			out.deleteCharAt(out.length() - 1); // delete last comma
		}

		out.append("]");

		return out.toString();
	}
}
