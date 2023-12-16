package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Album extends AudioCollection {
	private final ArrayList<Song> songs;

	public Album(final String name, final String owner, final ArrayList<Song> songs) {
		super(name, owner);
		this.songs = songs;
	}

	public boolean containsByName(final String name) {
		return getName().toLowerCase().startsWith(name.toLowerCase());
	}

	public boolean matchesAlbum(final String album) {
		return false;
	}

	@Override
	public int getNumberOfTracks() {
		return songs.size();
	}

	@Override
	public AudioFile getTrackByIndex(int index) {
		return songs.get(index);
	}
}
