package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Album extends AudioCollection {
    private final ArrayList<Song> songs;

    public Album(final String name, final String owner, final ArrayList<Song> songs) {
        super(name, owner);
        this.songs = songs;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    /**
     * Get the likes of an album
     *
     * @param object input album
     * @return likes count
     */
    public static int getLikes(final Object object) {
        int likes = 0;
        for (Song song : ((Album) object).getSongs()) {
            likes += song.getLikes();
        }

        return likes;
    }
}
