package com.example.cadenzabackend.service;


import com.example.cadenzabackend.model.Album;
import com.example.cadenzabackend.model.Artist;
import com.example.cadenzabackend.repository.AlbumRepository;
import com.example.cadenzabackend.repository.ArtistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public Optional<Album> getAlbumById(Long id) {
        return albumRepository.findById(id);
    }

    public List<Album> searchAlbums(String query) {
        List<Album> albumsByTitle = albumRepository.findByTitleContainingIgnoreCase(query);
        List<Album> albumsByArtist = albumRepository.findByArtistNameContainingIgnoreCase(query);

        Set<Album> combinedAlbums = new HashSet<>(albumsByTitle);
        combinedAlbums.addAll(albumsByArtist);

        return List.copyOf(combinedAlbums);
    }

    @Transactional
    public Album createAlbum(Album album) {
        if (album.getArtist() != null && album.getArtist().getId() != null) {
            Artist artist = artistRepository.findById(album.getArtist().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Artist not found for album creation"));
            album.setArtist(artist);
        } else {
            throw new IllegalArgumentException("Album must be associated with an existing artist.");
        }
        return albumRepository.save(album);
    }

    @Transactional
    public Album updateAlbum(Long id, Album updatedAlbum) {
        return albumRepository.findById(id).map(album -> {
            album.setTitle(updatedAlbum.getTitle());
            album.setReleaseDate(updatedAlbum.getReleaseDate());
            album.setCoverImageUrl(updatedAlbum.getCoverImageUrl());

            if (updatedAlbum.getArtist() != null && updatedAlbum.getArtist().getId() != null) {
                Artist artist = artistRepository.findById(updatedAlbum.getArtist().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Artist not found for album update"));
                album.setArtist(artist);
            }
            return albumRepository.save(album);
        }).orElseThrow(() -> new RuntimeException("Album not found with id " + id));
    }

    @Transactional
    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }
}
