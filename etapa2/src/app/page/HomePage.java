package app.page;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

public final class HomePage implements Page {
    private final User user;

    public HomePage(final User user) {
        this.user = user;
    }

    /**
     * Prints according to Home Page
     *
     * @return final string
     */
    public String print() {
        StringBuilder out = new StringBuilder();

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
