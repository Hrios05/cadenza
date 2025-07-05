package com.example.cadenzabackend.service;

import com.example.cadenzabackend.model.Song;
import com.example.cadenzabackend.model.Artist;
import com.example.cadenzabackend.model.Album;
import com.example.cadenzabackend.model.Genre;
import com.example.cadenzabackend.repository.SongRepository;
import com.example.cadenzabackend.repository.ArtistRepository;
import com.example.cadenzabackend.repository.AlbumRepository;
import com.example.cadenzabackend.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
public class SongService {
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;

    public SongService(SongRepository songRepository, ArtistRepository artistRepository, AlbumRepository albumRepository, GenreRepository genreRepository) {
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.genreRepository = genreRepository;
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Optional<Song> getSongById(Long id) {
        return songRepository.findById(id);
    }

    public List<Song> searchSongs(String query) {
        // Implement more sophisticated search logic here (e.g., combine results from title, artist, album)
        List<Song> songsByTitle = songRepository.findByTitleContainingIgnoreCase(query);
        List<Song> songsByArtist = songRepository.findByArtistNameContainingIgnoreCase(query);
        List<Song> songsByAlbum = songRepository.findByAlbumTitleContainingIgnoreCase(query);

        // Combine and deduplicate results
        Set<Song> combinedSongs = new HashSet<>(songsByTitle);
        combinedSongs.addAll(songsByArtist);
        combinedSongs.addAll(songsByAlbum);

        return List.copyOf(combinedSongs); // Return an immutable list
    }

    @Transactional
    public Song createSong(Song song) {
        // Ensure artist, album, and genre exist or are linked correctly
        if (song.getArtist() != null && song.getArtist().getId() != null) {
            Artist artist = artistRepository.findById(song.getArtist().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Artist not found"));
            song.setArtist(artist);
        }
        if (song.getAlbum() != null && song.getAlbum().getId() != null) {
            Album album = albumRepository.findById(song.getAlbum().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Album not found"));
            song.setAlbum(album);
        }
        if (song.getGenre() != null && song.getGenre().getId() != null) {
            Genre genre = genreRepository.findById(song.getGenre().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Genre not found"));
            song.setGenre(genre);
        }
        return songRepository.save(song);
    }

    @Transactional
    public Song updateSong(Long id, Song updatedSong) {
        return songRepository.findById(id).map(song -> {
            song.setTitle(updatedSong.getTitle());
            song.setDurationSeconds(updatedSong.getDurationSeconds());
            song.setAudioFileUrl(updatedSong.getAudioFileUrl());

            // Update associations if provided
            if (updatedSong.getArtist() != null && updatedSong.getArtist().getId() != null) {
                Artist artist = artistRepository.findById(updatedSong.getArtist().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Artist not found for update"));
                song.setArtist(artist);
            }
            if (updatedSong.getAlbum() != null && updatedSong.getAlbum().getId() != null) {
                Album album = albumRepository.findById(updatedSong.getAlbum().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Album not found for update"));
                song.setAlbum(album);
            }
            if (updatedSong.getGenre() != null && updatedSong.getGenre().getId() != null) {
                Genre genre = genreRepository.findById(updatedSong.getGenre().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Genre not found for update"));
                song.setGenre(genre);
            }
            return songRepository.save(song);
        }).orElseThrow(() -> new RuntimeException("Song not found with id " + id));
    }

    @Transactional
    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }

}
