package com.example.cadenzabackend.service;

import com.example.cadenzabackend.model.Artist;
import com.example.cadenzabackend.repository.ArtistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }

    public List<Artist> searchArtists(String query) {
        return artistRepository.findByNameContainingIgnoreCase(query);
    }

    @Transactional
    public Artist createArtist(Artist artist) {
        if (artistRepository.findByNameIgnoreCase(artist.getName()).isPresent()) {
            throw new IllegalArgumentException("Artist with this name already exists.");
        }
        return artistRepository.save(artist);
    }

    @Transactional
    public Artist updateArtist(Long id, Artist updatedArtist) {
        return artistRepository.findById(id).map(artist -> {
            artist.setName(updatedArtist.getName());
            artist.setBio(updatedArtist.getBio());
            artist.setImageUrl(updatedArtist.getImageUrl());
            return artistRepository.save(artist);
        }).orElseThrow(() -> new RuntimeException("Artist not found with id " + id));
    }

    @Transactional
    public void deleteArtist(Long id) {
        artistRepository.deleteById(id);
    }
}
