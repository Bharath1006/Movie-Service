package com.bharath.firstspringproject.service;

import com.bharath.firstspringproject.exception.InvalidDataException;
import com.bharath.firstspringproject.exception.NotFoundException;
import com.bharath.firstspringproject.model.Movie;
import com.bharath.firstspringproject.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setName("Inception");
        movie.setDirector("Christopher Nolan");
    }

    @Test
    void testCreateMovie() {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        Movie createdMovie = movieService.create(movie);
        assertNotNull(createdMovie);
        assertEquals("Inception", createdMovie.getName());
    }

    @Test
    void testReadMovie() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        Movie foundMovie = movieService.read(1L);
        assertNotNull(foundMovie);
        assertEquals("Inception", foundMovie.getName());
    }

    @Test
    void testGetAllMovies() {
        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie));
        List<Movie> movies = movieService.getAllMovies();
        assertFalse(movies.isEmpty());
        assertEquals(1, movies.size());
    }

    @Test
    void testUpdateMovie() {
        when(movieRepository.existsById(anyLong())).thenReturn(true);
        when(movieRepository.getReferenceById(anyLong())).thenReturn(movie);
        movieService.update(1L, movie);
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testDeleteMovie() {
        when(movieRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(movieRepository).deleteById(anyLong());
        movieService.delete(1L);
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreateMovie_NullMovie() {
        assertThrows(InvalidDataException.class, () -> movieService.create(null));
    }

    @Test
    void testReadMovie_NotFound() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> movieService.read(1L));
    }

    @Test
    void testUpdateMovie_NotFound() {
        when(movieRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> movieService.update(1L, movie));
    }

    @Test
    void testDeleteMovie_NotFound() {
        when(movieRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> movieService.delete(1L));
    }
}
