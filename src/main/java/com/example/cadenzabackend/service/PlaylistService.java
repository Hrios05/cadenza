package com.example.cadenzabackend.service;

import com.example.cadenzabackend.model.Playlist;
import com.example.cadenzabackend.model.PlaylistSong;
import com.example.cadenzabackend.model.Song;
import com.example.cadenzabackend.model.User;
import com.example.cadenzabackend.repository.PlaylistRepository;
import com.example.cadenzabackend.repository.PlaylistSongRepository;
import com.example.cadenzabackend.repository.SongRepository;
import com.example.cadenzabackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

import org.springframework.stereotype.Service;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public PlaylistService(PlaylistRepository playlistRepository, PlaylistSongRepository playlistSongRepository, UserRepository userRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.playlistSongRepository = playlistSongRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
    }

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Optional<Playlist> getPlaylistById(Long id) {
        return playlistRepository.findById(id);
    }

    public List<Playlist> getPlaylistsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return playlistRepository.findByUser(user);
    }

    public List<Song> getSongsInPlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found with id: " + playlistId));
        return playlistSongRepository.findByPlaylistOrderByOrderInPlaylistAsc(playlist)
                .stream()
                .map(PlaylistSong::getSong)
                .collect(Collectors.toList());
    }

    @Transactional
    public Playlist createPlaylist(Playlist playlist, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        playlist.setUser(user);
        return playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist updatePlaylist(Long id, Playlist updatedPlaylist) {
        return playlistRepository.findById(id).map(playlist -> {
            playlist.setName(updatedPlaylist.getName());
            playlist.setDescription(updatedPlaylist.getDescription());
            return playlistRepository.save(playlist);
        }).orElseThrow(() -> new RuntimeException("Playlist not found with id " + id));
    }

    @Transactional
    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }

    @Transactional
    public PlaylistSong addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found with id: " + playlistId));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found with id: " + songId));

        // Determine the next order in playlist
        Integer maxOrder = playlist.getPlaylistSongs().stream()
                .map(PlaylistSong::getOrderInPlaylist)
                .max(Comparator.nullsFirst(Integer::compare)) // Handle potential nulls if playlistSongs is empty or has null orders
                .orElse(0); // If no songs, start at 0 or 1

        PlaylistSong playlistSong = new PlaylistSong();
        playlistSong.setPlaylist(playlist);
        playlistSong.setSong(song);
        playlistSong.setOrderInPlaylist(maxOrder + 1);

        return playlistSongRepository.save(playlistSong);
    }

    @Transactional
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found with id: " + playlistId));
        // This method needs to be careful if there are multiple instances of the same song in a playlist
        // For simplicity, we'll delete the first matching entry.
        // A more robust solution might require a PlaylistSong ID or a way to identify specific entries.
        playlistSongRepository.deleteByPlaylistAndSongId(playlist, songId);
    }
}
