package app.audio.Collections;

import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;

public class Album {
	private ArrayList<Song> songs;
	@Getter
	private String name;

	public Album(ArrayList<Song> songs, String name) {
		this.songs = songs;
		this.name = name;
	}
}
