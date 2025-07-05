package com.example.cadenzabackend.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // The user who owns this playlist

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderInPlaylist ASC") // Order songs in playlist
    private Set<PlaylistSong> playlistSongs;

    // No-argument constructor
    public Playlist() {
    }

    // All-arguments constructor
    public Playlist(Long id, String name, String description, User user, Set<PlaylistSong> playlistSongs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
        this.playlistSongs = playlistSongs;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<PlaylistSong> getPlaylistSongs() {
        return playlistSongs;
    }

    public void setPlaylistSongs(Set<PlaylistSong> playlistSongs) {
        this.playlistSongs = playlistSongs;
    }

    // Override toString
    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", user=" + (user != null ? user.getId() : null) +
                ", playlistSongs=" + (playlistSongs != null ? playlistSongs.size() : 0) +
                '}';
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return id != null && id.equals(playlist.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}