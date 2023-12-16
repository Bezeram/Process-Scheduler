package app;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import app.audio.Collections.Album;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.page.HomePage;
import app.page.LikedContentPage;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.user.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private CommandRunner() {
    }

    /**
     * Changes the current page of the user to the one provided
     *
     * @param commandInput the command input
     * @return json node
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());

        assert user != null;
        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String page = commandInput.getNextPage();
        if (page != null) {
            switch (page) {
                case "Home" -> user.setCurrentPage(new HomePage(user));
                case "LikedContent" -> user.setCurrentPage(new LikedContentPage(user));
                default -> {
                    objectNode.put("message",
                            user.getUsername() + " is trying to access a non-existent page.");
                    return objectNode;
                }
            }
        }

        objectNode.put("message", user.getUsername() + " accessed " + page + " successfully.");
        return objectNode;
    }

    /**
     * Prints the user's current page
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        String username = commandInput.getUsername();
        User user = Admin.getUser(username);

        assert user != null;
        if (!user.isOnline()) {
            objectNode.put("message", username + " is offline.");
            return objectNode;
        }

        objectNode.put("message", user.getCurrentPage().print());
        return objectNode;
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        if (!user.isOnline()) {
            ArrayList<String> results = new ArrayList<>();
            objectNode.put("message",
                    user.getUsername() + " is offline.");
            objectNode.put("results",
                    OBJECT_MAPPER.valueToTree(results));
            return objectNode;
        }

        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = user.search(filters, type);
        String message = "Search returned " + results.size() + " results";

        objectNode.put("message", message);
        objectNode.put("results", OBJECT_MAPPER.valueToTree(results));

        return objectNode;
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (!user.isOnline()) {
            String message = user.getUsername() + " is offline.";
            objectNode.put("message", message);
            return objectNode;
        }

        String message = user.select(commandInput.getItemNumber());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        User user = Admin.getUser(commandInput.getUsername());
        if (!user.isOnline()) {
            String message = user.getUsername() + " is offline.";
            objectNode.put("message", message);
            return objectNode;
        }

        String message = user.load();
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        User user = Admin.getUser(commandInput.getUsername());
        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.playPause();
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        User user = Admin.getUser(commandInput.getUsername());
        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.repeat();
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.forward();
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.backward();
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.like();
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.next();
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.prev();
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            return objectNode;
        }

        String message = user.follow();
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", OBJECT_MAPPER.valueToTree(stats));

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 albums.
     *
     * @param commandInput the command input
     * @return the top 5 albums
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> albums = Admin.getTop5Albums();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(albums));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", OBJECT_MAPPER.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Adds a user via username as ID.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", username);

        User user = Admin.getUser(username);
        if (user == null) {
            Admin.addUser(UserFactory.createUser(commandInput.getType(),
                    commandInput.getUsername(), commandInput.getAge(), commandInput.getCity()));
            objectNode.put("message",
                    "The username " + username + " has been added successfully.");
            return objectNode;
        }

        objectNode.put("message", "The username " + username + " is already taken.");
        return objectNode;
    }

    /**
     * Deletes a user and its entities.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        String inputUsername = commandInput.getUsername();
        User inputUser = Admin.getUser(inputUsername);
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", inputUsername);

        if (inputUser == null) {
            objectNode.put("message", "The username " + inputUsername + " doesn't exist.");
            return objectNode;
        }

        if (inputUser.isSafeToDelete()) {
            Admin.deleteUser(inputUsername);

            objectNode.put("message", inputUsername + " was successfully deleted.");
            return objectNode;
        }

        objectNode.put("message", inputUsername + " can't be deleted.");
        return objectNode;
    }

    /**
     * Gets all the online users.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        ArrayList<String> onlineUsers = new ArrayList<>();
        for (User user : Admin.getUsers()) {
            if (user.isOnline()) {
                onlineUsers.add(user.getUsername());
            }
        }

        objectNode.put("result", OBJECT_MAPPER.valueToTree(onlineUsers));
        return objectNode;
    }

    /**
     * Toggles between online and offline for a regular user.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Artist || user instanceof Host) {
            objectNode.put(
                    "message", commandInput.getUsername() + " is not a normal user.");
            return objectNode;
        }

        user.switchConnectionStatus();
        objectNode.put("message",
                commandInput.getUsername() + " has changed status successfully.");
        return objectNode;
    }

    /**
     * Adds an album.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Artist artist) {
            String albumName = commandInput.getName();

            if (artist.hasAlbumByName(albumName)) {
                objectNode.put("message",
                        commandInput.getUsername() + " has another album with the same name.");
                return objectNode;
            }

            ArrayList<SongInput> songs = commandInput.getSongs();
            for (int i = 0; i < songs.size() - 1; i++) {
                for (int j = i + 1; j < songs.size(); j++) {
                    SongInput songI = songs.get(i);
                    SongInput songJ = songs.get(j);
                    if (songI.getName().equalsIgnoreCase(songJ.getName())) {
                        objectNode.put("message",
                        commandInput.getUsername() + " has the same song at "
                                + "least twice in this album.");
                        return objectNode;
                    }
                }
            }

            ArrayList<Song> songData = new ArrayList<>();
            for (SongInput songInput : songs) {
                Song song = new Song(songInput.getName(), songInput.getDuration(),
                        songInput.getAlbum(), songInput.getTags(),
                        songInput.getLyrics(), songInput.getGenre(),
                        songInput.getReleaseYear(), songInput.getArtist());
                songData.add(song);
            }

            Album newAlbum = new Album(albumName, artist.getUsername(), songData);

            artist.addAlbum(newAlbum);
            Admin.addAlbum(newAlbum);
            Admin.addSongs(songData);

            objectNode.put("message",
                    commandInput.getUsername() + " has added new album successfully.");
            return objectNode;
        }

        objectNode.put("message", commandInput.getUsername() + " is not an artist.");
        return objectNode;
    }

    /**
     * Returns all the albums
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        Artist artist = (Artist) Admin.getUser(commandInput.getUsername());

        ArrayList<ShowAlbumsResult> result = new ArrayList<>();
        for (Album album : artist.getAlbums()) {
            ShowAlbumsResult entry = new ShowAlbumsResult();

            entry.name = album.getName();
            for (Song song : album.getSongs()) {
                entry.songs.add(song.getName());
            }

            result.add(entry);
        }

        objectNode.put("result", OBJECT_MAPPER.valueToTree(result));
        return objectNode;
    }

    /**
     * Adds an event.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Artist artist) {
            String eventName = commandInput.getName();

            if (artist.hasEventByName(eventName)) {
                objectNode.put("message",
                commandInput.getUsername() + " has another event with the same name.");
                return objectNode;
            }

            if (!isValidDate(commandInput.getDate())) {
                objectNode.put("message",
                "Event for " + commandInput.getUsername() + " does not have a valid date.");
                return objectNode;
            }

            artist.addEvent(new Artist.Event(
                    eventName, commandInput.getDate(), commandInput.getDescription()));
            objectNode.put("message",
                    commandInput.getUsername() + " has added new event successfully.");
            return objectNode;
        }

        objectNode.put("message",
                commandInput.getUsername() + " is not an artist.");
        return objectNode;
    }

    /**
     * Helper function for determining if a given string is a valid date
     *
     * @param dateString the date string
     * @return true / false
     */
    private static boolean isValidDate(final String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Adds merch.
     *
     * @param commandInput the date string
     * @return the object node
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message",
            "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Artist artist) {
            String merchName = commandInput.getName();

            if (artist.hasMerchByName(merchName)) {
                objectNode.put("message",
                commandInput.getUsername() + " has merchandise with the same name.");
                return objectNode;
            }

            if (commandInput.getPrice() < 0) {
                objectNode.put("message", "Price for merchandise can not be negative.");
                return objectNode;
            }

            artist.addMerch(new Artist.Merch(
                    merchName, commandInput.getDescription(), commandInput.getPrice()));
            objectNode.put("message",
            commandInput.getUsername() + " has added new merchandise successfully.");
            return objectNode;
        }

        objectNode.put("message",
                commandInput.getUsername() + " is not an artist.");
        return objectNode;
    }

    /**
     * Returns all the users.
     *
     * @param commandInput the date string
     * @return the object node
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        List<User> users = Admin.getUsers();
        ArrayList<String> usersNames = new ArrayList<>();
        users.sort((user1, user2) -> {
            int score1 = 0;
            int score2 = 0;
            if (user1 instanceof Artist) {
                score1 = 1;
            } else if (user1 instanceof Host) {
                score1 = 2;
            }

            if (user2 instanceof Artist) {
                score2 = 1;
            } else if (user2 instanceof Host) {
                score2 = 2;
            }
            return score1 - score2;
        });

        for (User user : users) {
            usersNames.add(user.getUsername());
        }

        objectNode.put("result", OBJECT_MAPPER.valueToTree(usersNames));
        return objectNode;
    }

    /**
     * Adds a podcast.
     *
     * @param commandInput the date string
     * @return the object node
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Host host) {
            String podcastName = commandInput.getName();

            if (host.hasPodcastByName(podcastName)) {
                objectNode.put("message",
                commandInput.getUsername() + " has another podcast with the same name.");
                return objectNode;
            }

            ArrayList<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : commandInput.getEpisodes()) {
                if (host.createdEpisodeByName(episodeInput.getName())) {
                    objectNode.put("message",
                    commandInput.getUsername() + " has the same episode in this podcast.");
                    return objectNode;
                }

                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }

            Podcast podcast = new Podcast(podcastName, host.getName(), episodes);
            host.addPodcast(podcast);
            Admin.addPodcast(podcast);
            objectNode.put("message",
            commandInput.getUsername() + " has added new podcast successfully.");
            return objectNode;
        }

        objectNode.put("message",
        commandInput.getUsername() + " is not a host.");
        return objectNode;
    }

    /**
     * Adds announcement.
     *
     * @param commandInput the date string
     * @return the object node
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message",
            "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Host host) {
            String announcementName = commandInput.getName();

            if (host.hasAnnouncementByName(announcementName)) {
                objectNode.put("message",
                commandInput.getUsername() + " has already"
                        + " added an announcement with this name.");
                return objectNode;
            }

            Host.Announcement announcement = new
                Host.Announcement(announcementName, commandInput.getDescription());
            host.addAnnouncement(announcement);
            objectNode.put("message",
            commandInput.getUsername() + " has successfully added new announcement.");
            return objectNode;
        }

        objectNode.put("message",
        commandInput.getUsername() + " is not a host.");
        return objectNode;
    }

    /**
     * Removes an announcement.
     *
     * @param commandInput the date string
     * @return the object node
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Host host) {
            String announcementName = commandInput.getName();

            if (!host.hasAnnouncementByName(announcementName)) {
                objectNode.put("message",
                commandInput.getUsername() + " has no announcement with the given name.");
                return objectNode;
            }

            host.removeAnnouncement(announcementName);
            objectNode.put("message",
            commandInput.getUsername() + " has successfully deleted the announcement.");
            return objectNode;
        }

        objectNode.put("message",
                commandInput.getUsername() + " is not a host.");
        return objectNode;
    }

    /**
     * Returns all podcasts.
     *
     * @param commandInput the date string
     * @return the object node
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        Host host = (Host) Admin.getUser(commandInput.getUsername());

        ArrayList<ShowPodcastResult> result = new ArrayList<>();
        for (Podcast podcast : host.getPodcasts()) {
            ShowPodcastResult entry = new ShowPodcastResult();

            entry.name = podcast.getName();
            for (Episode episode : podcast.getEpisodes()) {
                entry.episodes.add(episode.getName());
            }

            result.add(entry);
        }

        objectNode.put("result", OBJECT_MAPPER.valueToTree(result));
        return objectNode;
    }

    /**
     * Removes an album if no other user is loading an episode from it
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Host host) {
            String albumName = commandInput.getName();

            if (!host.hasPodcastByName(albumName)) {
                objectNode.put("message",
                commandInput.getUsername() + " doesn't have a podcast with the given name.");
                return objectNode;
            }

            if (!host.isPodcastSafeToDelete(albumName)) {
                objectNode.put("message",
                commandInput.getUsername() + " can't delete this podcast.");
                return objectNode;
            }

            host.removePodcast(albumName);
            Admin.removePodcast(albumName);

            objectNode.put("message",
                    commandInput.getUsername() + " deleted the podcast successfully.");
            return objectNode;
        }

        objectNode.put("message",
                commandInput.getUsername() + " is not a host.");
        return objectNode;
    }

    /**
     * Removes an album if no other user is loading it or a song from it
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Artist artist) {
            String albumName = commandInput.getName();

            if (!artist.hasAlbumByName(albumName)) {
                objectNode.put("message",
                commandInput.getUsername() + " doesn't have an album with the given name.");
                return objectNode;
            }

            if (!artist.isAlbumSafeToDelete(albumName)) {
                objectNode.put("message",
                commandInput.getUsername() + " can't delete this album.");
                return objectNode;
            }

            artist.removeAlbum(albumName);
            Admin.removeAlbum(albumName);

            objectNode.put("message",
            commandInput.getUsername() + " deleted the album successfully.");
            return objectNode;
        }

        objectNode.put("message",
        commandInput.getUsername() + " is not an artist.");
        return objectNode;
    }

    /**
     * Removes an event.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message",
                    "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Artist artist) {
            String announcementName = commandInput.getName();

            if (!artist.hasEventByName(announcementName)) {
                objectNode.put("message",
                commandInput.getUsername() + " doesn't have an event with the given name.");
                return objectNode;
            }

            artist.removeEvent(announcementName);
            objectNode.put("message",
            commandInput.getUsername() + " deleted the event successfully.");
            return objectNode;
        }

        objectNode.put("message",
                commandInput.getUsername() + " is not an artist.");
        return objectNode;
    }

    @Getter
    @Setter
    public static class ShowAlbumsResult {
        private String name;
        private ArrayList<String> songs = new ArrayList<>();
    }

    @Setter
    @Getter
    public static class ShowPodcastResult {
        private String name;
        private ArrayList<String> episodes = new ArrayList<>();
    }
}
