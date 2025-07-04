package com.example.cadenzabackend.repository;
import com.example.cadenzabackend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long>{
    List<Song> findByTitleContainingIgnoreCase(String title);
    List<Song> findByArtistNameContainingIgnoreCase(String artistName);
    List<Song> findByAlbumTitleContainingIgnoreCase(String albumTitle);
}
