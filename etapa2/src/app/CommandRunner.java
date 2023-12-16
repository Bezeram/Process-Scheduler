package app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import app.audio.Collections.Album;
import app.audio.Collections.PlaylistOutput;
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
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    private CommandRunner() {
    }

    /**
     * Changes the current page of the user to the one provided
     *
     * @param commandInput the command input
     * @return json node
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
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
                    objectNode.put("message", user.getUsername() + " is trying to access a non-existent page.");
                    return objectNode;
                }
            }
        }

        objectNode.put("message", user.getUsername() + " accessed " + page + "successfully.");
        return objectNode;
    }

    /**
     * Prints the user's current page
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        String username = commandInput.getUsername();
        User user = Admin.getUser(username);

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
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (!user.isOnline()) {
            ArrayList<String> results = new ArrayList<>();
            objectNode.put("message", user.getUsername() + " is offline.");
            objectNode.put("results", objectMapper.valueToTree(results));
            return objectNode;
        }

        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = user.search(filters, type);
        String message = "Search returned " + results.size() + " results";

        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
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
        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        User user = Admin.getUser(commandInput.getUsername());
        if (user.isOnline()) {
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

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user.isOnline()) {
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

        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

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

        ObjectNode objectNode = objectMapper.createObjectNode();
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

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

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

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

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

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

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

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

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

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Adds a user via username as ID
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        ObjectNode objectNode = objectMapper.createObjectNode();
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

    public static ObjectNode deleteUser(final CommandInput commandInput) {
        String inputUsername = commandInput.getUsername();
        User inputUser = Admin.getUser(inputUsername);
        ObjectNode objectNode = objectMapper.createObjectNode();
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

    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        ArrayList<String> onlineUsers = new ArrayList<>();
        for (User user : Admin.getUsers())
            if (user.isOnline())
                onlineUsers.add(user.getUsername());

        objectNode.put("result", objectMapper.valueToTree(onlineUsers));
        return objectNode;
    }

    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Artist || user instanceof Host) {
            objectNode.put("message", commandInput.getUsername() + " is not a normal user.");
            return objectNode;
        }

        user.switchConnectionStatus();
        objectNode.put("message", commandInput.getUsername() + " has changed status successfully.");
        return objectNode;
    }

    public static ObjectNode addAlbum(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Artist artist) {
            String albumName = commandInput.getName();

            if (artist.hasAlbumByName(albumName)) {
                objectNode.put("message", commandInput.getUsername() + " has another album with the same name.");
                return objectNode;
            }

            ArrayList<SongInput> songs = commandInput.getSongs();
            for (int i = 0; i < songs.size() - 1; i++)
                for (int j = i + 1; j < songs.size(); j++) {
                    SongInput songI = songs.get(i);
                    SongInput songJ = songs.get(j);
                    if (songI.getName().equalsIgnoreCase(songJ.getName())) {
                        objectNode.put("message", commandInput.getUsername() + " has the same song at least twice in this album.");
                        return objectNode;
                    }
                }

            ArrayList<Song> songData = new ArrayList<>();
            for (SongInput songInput : songs) {
                Song song = new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                        songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                        songInput.getReleaseYear(), songInput.getArtist());
                songData.add(song);
            }

            Album newAlbum = new Album(albumName, artist.getUsername(), songData);

            artist.addAlbum(newAlbum);
            Admin.addAlbum(newAlbum);
            Admin.addSongs(songData);

            objectNode.put("message", commandInput.getUsername() + " has added new album successfully.");
            return objectNode;
        }

        objectNode.put("message", commandInput.getUsername() + " is not an artist.");
        return objectNode;
    }

    public static ObjectNode showAlbums(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        Artist artist = (Artist) Admin.getUser(commandInput.getUsername());

        ArrayList<ShowAlbumsResult> result = new ArrayList<>();
        for (Album album : artist.getAlbums()) {
            ShowAlbumsResult entry = new ShowAlbumsResult();

            entry.name = album.getName();
            for (Song song : album.getSongs())
                entry.songs.add(song.getName());

            result.add(entry);
        }

        objectNode.put("result", objectMapper.valueToTree(result));
        return objectNode;
    }

    public static ObjectNode addEvent(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Artist artist) {
            String eventName = commandInput.getName();

            if (artist.hasEventByName(eventName)) {
                objectNode.put("message", commandInput.getUsername() + " has another event with the same name.");
                return objectNode;
            }

            if (!isValidDate(commandInput.getDate())) {
                objectNode.put("message", "Event for " + commandInput.getUsername() + "does not have a valid date.");
                return objectNode;
            }

            artist.addEvent(new Artist.Event(eventName, commandInput.getDate(), commandInput.getDescription()));
            objectNode.put("message", commandInput.getUsername() + " has added new event successfully.");
            return objectNode;
        }

        objectNode.put("message", commandInput.getUsername() + " is not an artist.");
        return objectNode;
    }

    private static boolean isValidDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static ObjectNode addMerch(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }

        if (user instanceof Artist artist) {
            String merchName = commandInput.getName();

            if (artist.hasMerchByName(merchName)) {
                objectNode.put("message", commandInput.getUsername() + " has merchandise with the same name.");
                return objectNode;
            }

            if (commandInput.getPrice() < 0) {
                objectNode.put("message", "Price for merchandise can not be negative.");
                return objectNode;
            }

            artist.addMerch(new Artist.Merch(merchName, commandInput.getDescription(), commandInput.getPrice()));
            objectNode.put("message", commandInput.getUsername() + " has added new merchandise successfully.");
            return objectNode;
        }

        objectNode.put("message", commandInput.getUsername() + " is not an artist.");
        return objectNode;
    }

    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        List<User> users = Admin.getUsers();
        ArrayList<String> usersNames = new ArrayList<>();
        users.sort((user1, user2) -> {
            int score1 = 0;
            int score2 = 0;
            if (user1 instanceof Artist)
                score1 = 1;
            else if (user1 instanceof Host)
                score1 = 2;

            if (user2 instanceof Artist)
                score2 = 1;
            else if (user2 instanceof Host)
                score2 = 2;
            return score1 - score2;
        });

        for (User user : users)
            usersNames.add(user.getUsername());

        objectNode.put("result", objectMapper.valueToTree(usersNames));
        return objectNode;
    }

    public static class ShowAlbumsResult {
        public String name;
        public ArrayList<String> songs = new ArrayList<>();
    }
}
