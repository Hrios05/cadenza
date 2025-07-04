package com.example.cadenzabackend.repository;
import com.example.cadenzabackend.model.Album;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface  AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByTitleContainingIgnoreCase(String title);
    List<Album> findByArtistNameContainingIgnoreCase(String artistName);
}
