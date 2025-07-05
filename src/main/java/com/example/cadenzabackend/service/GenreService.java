package com.example.cadenzabackend.service;



import com.example.cadenzabackend.model.Genre;
import com.example.cadenzabackend.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    @Transactional
    public Genre createGenre(Genre genre) {
        if (genreRepository.findByNameIgnoreCase(genre.getName()).isPresent()) {
            throw new IllegalArgumentException("Genre with this name already exists.");
        }
        return genreRepository.save(genre);
    }

    @Transactional
    public Genre updateGenre(Long id, Genre updatedGenre) {
        return genreRepository.findById(id).map(genre -> {
            genre.setName(updatedGenre.getName());
            return genreRepository.save(genre);
        }).orElseThrow(() -> new RuntimeException("Genre not found with id " + id));
    }

    @Transactional
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
