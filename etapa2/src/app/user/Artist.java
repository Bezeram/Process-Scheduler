package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.page.ArtistPage;
import app.player.PlayerStats;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Artist extends User {
    private final ArrayList<Merch> merch = new ArrayList<>();
    private final ArrayList<Event> events = new ArrayList<>();
    private final ArrayList<Album> albums = new ArrayList<>();

    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        online = false;
    }

    /**
     * Adds merch
     *
     * @param merchIn merch
     */
    public void addMerch(final Merch merchIn) {
        merch.add(merchIn);
    }

    /**
     * Adds album
     *
     * @param albumIn album
     */
    public void addAlbum(final Album albumIn) {
        albums.add(albumIn);
    }

    /**
     * Adds event
     *
     * @param eventIn event
     */
    public void addEvent(final Event eventIn) {
        events.add(eventIn);
    }

    /**
     * Checks if it has album via name
     *
     * @param albumName album name
     * @return true / false
     */
    public boolean hasAlbumByName(final String albumName) {
        for (Album album : albums) {
            if (album.getName().equalsIgnoreCase(albumName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * It is NOT safe to delete the album if a user's player is running and
     * the current song is of the artist's
     *
     * @param albumName the album name is always valid
     * @return true / false
     */
    public boolean isAlbumSafeToDelete(final String albumName) {
        boolean safeDelete = true;
        Album match = null;
        for (Album album : albums) {
            if (album.getName().equalsIgnoreCase(albumName)) {
                match = album;
            }
        }

        for (User user : Admin.getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)
                    || user.player.getCurrentAudioFile() == null) {
                continue;
            }

            PlayerStats playerStats = user.getPlayerStats();
            if (!playerStats.isPaused()) {
                safeDelete = false;
                break;
            }

            for (Song song : match.getSongs()) {
                if (song.getName().equalsIgnoreCase(
                        user.player.getCurrentAudioFile().getName())) {
                    safeDelete = false;
                    break;
                }
            }
            if (!safeDelete) {
                break;
            }
        }

        return safeDelete;
    }

    /**
     * It is NOT safe to delete the artist if:<br>
     * - A user's player is running and the current song is of the artist's<br>
     * - A user's selected page is the artist's
     *
     * @return true / false
     */
    @Override
    public boolean isSafeToDelete() {
        boolean safeDelete = true;

        for (User user : Admin.getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                continue;
            }

            if (user.player.getCurrentAudioFile() != null) {
                PlayerStats playerStats = user.getPlayerStats();
                if (!playerStats.isPaused()
                    && this.createdSongByName(user.player.getCurrentAudioFile().getName())) {
                    safeDelete = false;
                    break;
                }
            }

            if (user.getCurrentPage() instanceof ArtistPage artistPage
                    && artistPage.artist() == this) {
                safeDelete = false;
                break;
            }
        }

        return safeDelete;
    }

    /**
     * Checks to see if artist has created a song with the name provided
     *
     * @param songName the song name
     * @return true / false
     */
    public boolean createdSongByName(final String songName) {
        for (Album album : albums) {
            for (Song song : album.getSongs()) {
                if (song.getName().equalsIgnoreCase(songName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks to see if artist has created an event with the name provided
     *
     * @param eventName the event name
     * @return true / false
     */
    public boolean hasEventByName(final String eventName) {
        for (Event event : events) {
            if (event.name().equalsIgnoreCase(eventName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if artist has created merch with the name provided
     *
     * @param merchName the merch name
     * @return true / false
     */
    public boolean hasMerchByName(final String merchName) {
        for (Merch merchEntry : merch) {
            if (merchEntry.name().equalsIgnoreCase(merchName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get Album via name
     *
     * @param albumName the album name
     * @return true / false
     */
    public Album getAlbumByName(final String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Removes an album.<br>
     * Album always exists.
     *
     * @param albumName the album name
     */
    public void removeAlbum(final String albumName) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getName().equalsIgnoreCase(albumName)) {
                albums.remove(i);
                return;
            }
        }
    }

    /**
     * Removes an announcement.<br>
     * Announcement always exists.
     *
     * @param announcementName the announcement name
     */
    public void removeEvent(final String announcementName) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).name().equalsIgnoreCase(announcementName)) {
                events.remove(i);
                return;
            }
        }
    }

    public record Merch(String name, String description, int price) {
    }

    public record Event(String name, String date, String description) {
    }
}
