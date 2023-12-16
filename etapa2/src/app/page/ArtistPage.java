package app.page;

import app.audio.Collections.Album;
import app.user.Artist;

public record ArtistPage(Artist artist) implements Page {

    /**
     * Prints according to ArtistPage
     *
     * @return final string
     */
    public String print() {
        StringBuilder out = new StringBuilder();

        out.append("Albums:\n\t[");
        for (Album album : artist.getAlbums()) {
            out.append(album.getName());
            out.append(", ");
        }
        if (!artist.getAlbums().isEmpty()) {
            out.deleteCharAt(out.length() - 1); // delete last space
            out.deleteCharAt(out.length() - 1); // delete last comma
        }

        out.append("]\n\nMerch:\n\t[");
        for (Artist.Merch merch : artist.getMerch()) {
            out.append(merch.name());
            out.append(" - ");
            out.append(merch.price());
            out.append(":\n\t");
            out.append(merch.description());
            out.append(", ");
        }
        if (!artist.getMerch().isEmpty()) {
            out.deleteCharAt(out.length() - 1); // delete last space
            out.deleteCharAt(out.length() - 1); // delete last comma
        }

        out.append("]\n\nEvents:\n\t[");
        for (Artist.Event event : artist.getEvents()) {
            out.append(event.name());
            out.append(" - ");
            out.append(event.date());
            out.append(":\n\t");
            out.append(event.description());
            out.append(", ");
        }
        if (!artist.getEvents().isEmpty()) {
            out.deleteCharAt(out.length() - 1); // delete last space
            out.deleteCharAt(out.length() - 1); // delete last comma
        }

        out.append("]");

        return out.toString();
    }
}
