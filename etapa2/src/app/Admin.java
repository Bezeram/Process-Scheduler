package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The type Admin.
 */
public final class Admin {
    @Getter
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static List<Album> albums = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;

    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Adds user. Assumes user is new
     *
     * @param user the input user
     */
    public static void addUser(final User user) {
        users.add(user);
    }


    /**
     * Deletes user and all of its data. Assumes it exists
     *
     * @param inputUsername the user's name
     */
    public static void deleteUser(final String inputUsername) {
        User user = getUser(inputUsername);

        if (user instanceof Artist artist) {
            for (int i = 0; i < songs.size();) {
                Album album = artist.getAlbumByName(songs.get(i).getAlbum());
                if (album == null || !album.getOwner().equals(artist.getName())) {
                    i++;
                    continue;
                }

                songs.remove(i);
            }

            // For every user, delete every mention of an invalid liked song
            for (User iterUser : getUsers()) {
                ArrayList<Song> favSongs = iterUser.getLikedSongs();
                for (int i = 0; i < favSongs.size(); i++) {
                    Album album = artist.getAlbumByName(favSongs.get(i).getAlbum());
                    if (album == null || !album.getOwner().equals(artist.getName())) {
                        i++;
                        continue;
                    }

                    favSongs.remove(i);
                }
            }

            for (int i = 0; i < albums.size();) {
                if (albums.get(i).getOwner().equals(artist.getName())) {
                    albums.remove(i);
                } else {
                    i++;
                }
            }
        } else if (user instanceof Host host) {
            for (int i = 0; i < podcasts.size();) {
                if (podcasts.get(i).getOwner().equals(host.getName())) {
                    podcasts.remove(i);
                } else {
                    i++;
                }
            }
        }

        // For every user, delete every mention of an invalid followed playlist
        for (User iterUser : getUsers()) {
            ArrayList<Playlist> followedPlaylists = iterUser.getFollowedPlaylists();
            for (int i = 0; i < followedPlaylists.size(); i++) {
                Playlist playlist = followedPlaylists.get(i);
                if (playlist == null || !playlist.getOwner().equals(user.getName())) {
                    i++;
                    continue;
                }

                followedPlaylists.remove(i);
            }
        }

        // For every playlist the user follows, decrease it's follow count
        for (Playlist playlist : user.getFollowedPlaylists()) {
            playlist.decreaseFollowers();
        }

        // Delete user
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName().equals(user.getName())) {
                users.remove(i);
                break;
            }
        }
    }

    /**
     * Removes an album and all of its associated songs<br>
     * Album always exists
     *
     * @param albumName the album name
     */
    public static void removeAlbum(final String albumName) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getName().equalsIgnoreCase(albumName)) {
                // Remove associated songs
                for (Song albumSong : albums.get(i).getSongs()) {
                    for (int j = 0; j < songs.size();) {
                        String albumSongName = albumSong.getName();
                        String songName = songs.get(j).getName();

                        if (albumSongName.equalsIgnoreCase(songName)) {
                            songs.remove(i);
                        } else {
                            i++;
                        }
                    }
                }

                albums.remove(i);
                return;
            }
        }
    }

    /**
     * Removes a podcast<br>
     * Podcast always exists
     *
     * @param podcastName the podcast name
     */
    public static void removePodcast(final String podcastName) {
        for (int i = 0; i < podcasts.size(); i++) {
            if (podcasts.get(i).getName().equalsIgnoreCase(podcastName)) {
                podcasts.remove(i);
                return;
            }
        }
    }

    /**
     * Sets songs and albums
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        albums = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            Song song = new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist());
            songs.add(song);
        }
    }

    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Adds songs.
     */
    public static void addSongs(final ArrayList<Song> songsInput) {
        songs.addAll(songsInput);
    }

    /**
     * Adds an album.
     */
    public static void addAlbum(final Album album) {
        Admin.albums.add(album);
    }

    /**
     * Adds a podcast.
     */
    public static void addPodcast(final Podcast podcast) {
        Admin.podcasts.add(podcast);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets albums
     *
     * @return the albums
     */
    public static List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 albums.
     *
     * @return the top 5 albums
     */
    public static List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(albums);
        sortedAlbums.sort(Comparator.comparingInt(Album::getLikes).reversed());
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= LIMIT) {
                break;
            }

            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }


}
