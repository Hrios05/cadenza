package com.example.cadenzabackend.controller;


import com.example.cadenzabackend.model.Playlist;
import com.example.cadenzabackend.model.Song;
import com.example.cadenzabackend.model.PlaylistSong;
import com.example.cadenzabackend.service.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    // Helper to get current authenticated user's username
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null; // Or throw an exception if user is not authenticated
    }

    // This method needs to be modified to get the current user's ID
    // For now, it's a placeholder. You'll need a UserService method to get User by username.
    // private Long getCurrentUserId() {
    //     String username = getCurrentUsername();
    //     // Fetch user from DB using username and return ID
    //     // Example: return userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")).getId();
    //     return 1L; // Placeholder for now
    // }

    @GetMapping
    public List<Playlist> getAllPlaylists() {
        // In a real app, you might want to only show public playlists or playlists by current user
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Long id) {
        Optional<Playlist> playlist = playlistService.getPlaylistById(id);
        return playlist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{playlistId}/songs")
    public List<Song> getSongsInPlaylist(@PathVariable Long playlistId) {
        return playlistService.getSongsInPlaylist(playlistId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Playlist createPlaylist(@RequestBody Playlist playlist) {
        // This needs to be linked to the authenticated user's ID
        // For now, assuming user ID 1 for testing purposes.
        // In a real app, you'd get the user ID from the JWT token/SecurityContext.
        Long currentUserId = 1L; // Placeholder - REPLACE WITH ACTUAL USER ID FROM AUTHENTICATION
        return playlistService.createPlaylist(playlist, currentUserId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Long id, @RequestBody Playlist playlist) {
        try {
            Playlist updatedPlaylist = playlistService.updatePlaylist(id, playlist);
            return ResponseEntity.ok(updatedPlaylist);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistSong addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        return playlistService.addSongToPlaylist(playlistId, songId);
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        playlistService.removeSongFromPlaylist(playlistId, songId);
    }
}
