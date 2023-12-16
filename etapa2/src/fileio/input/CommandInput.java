package fileio.input;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class CommandInput {
    private String command;
    private String username;
    private Integer timestamp;
    private String type; // song / playlist / podcast
    private FiltersInput filters; // pentru search
    private Integer itemNumber; // pentru select
    private Integer repeatMode; // pentru repeat
    private Integer playlistId; // pentru add/remove song
    private String playlistName; // pentru create playlist
    private Integer seed; // pentru shuffle
    private String nextPage; // pentru changePage
    private int age; // pentru addUser
    private String city; // pentru addUser
    private String name; // pentru addAlbum, removeAlbum, addEvent, removeEvent, addPodcast, removePodcast, etc
    private int releaseYear; // pentru addAlbum
    private String description; // pentru addAlbum, addEvent, addMerch, addAnnouncement
    private ArrayList<SongInput> songs; // pentru addAlbum
    private String date; // pentru addEvent
    private int price; // pentru addMerch
    private ArrayList<EpisodeInput> episodes; // pentru addPodcast

    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username + '\''
                + ", timestamp=" + timestamp
                + ", type='" + type + '\''
                + ", filters=" + filters
                + ", itemNumber=" + itemNumber
                + ", repeatMode=" + repeatMode
                + ", playlistId=" + playlistId
                + ", playlistName='" + playlistName + '\''
                + ", seed=" + seed
                + '}';
    }
}
