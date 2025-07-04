package com.example.cadenzabackend.repository;
import com.example.cadenzabackend.model.PlaylistSong;
import com.example.cadenzabackend.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {
    List<PlaylistSong> findByPlaylistOrderByOrderInPlaylistAsc(Playlist playlist);
    void deleteByPlaylistAndSongId(Playlist playlist, Long songId);
}
