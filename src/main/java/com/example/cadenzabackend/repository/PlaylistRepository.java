package com.example.cadenzabackend.repository;
import com.example.cadenzabackend.model.Playlist;

import com.example.cadenzabackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUser(User user);
    List<Playlist> findByNameContainingIgnoreCase(String name);
}
