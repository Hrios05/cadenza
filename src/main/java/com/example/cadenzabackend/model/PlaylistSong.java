package com.example.cadenzabackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "playlist_songs")
public class PlaylistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;

    private Integer orderInPlaylist; // To maintain song order in a playlist

    // No-argument constructor
    public PlaylistSong() {
    }

    // All-arguments constructor
    public PlaylistSong(Long id, Playlist playlist, Song song, Integer orderInPlaylist) {
        this.id = id;
        this.playlist = playlist;
        this.song = song;
        this.orderInPlaylist = orderInPlaylist;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Integer getOrderInPlaylist() {
        return orderInPlaylist;
    }

    public void setOrderInPlaylist(Integer orderInPlaylist) {
        this.orderInPlaylist = orderInPlaylist;
    }

    // Override toString
    @Override
    public String toString() {
        return "PlaylistSong{" +
                "id=" + id +
                ", playlist=" + (playlist != null ? playlist.getId() : null) +
                ", song=" + (song != null ? song.getId() : null) +
                ", orderInPlaylist=" + orderInPlaylist +
                '}';
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistSong that = (PlaylistSong) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}