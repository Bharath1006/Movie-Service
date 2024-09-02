package com.bharath.firstspringproject.service;

import com.bharath.firstspringproject.model.Movie;
import com.bharath.firstspringproject.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    //Create
    public Movie create(Movie movie)
    {
        if(movie == null) throw new RuntimeException("No movie data recieved to add movie");
        return movieRepository.save(movie);
    }

    //Readbyid
    public Movie read(Long id){
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error"));
    }

    //fetch all records
    public List<Movie> getAllMovies()
    {
        return movieRepository.findAll();

    }
    //update
    public void update(Long id, Movie update)
    {
        if (id == null || update ==null) {
            throw new RuntimeException("Error");
        }
        if (movieRepository.existsById(id))
        {
            Movie movie = movieRepository.getReferenceById(id);
            movie.setName(update.getName());
            movie.setDirector(update.getDirector());
            movie.setActors(update.getActors());
            movieRepository.save(movie);
        }
        else {
            throw new RuntimeException("Update error");
        }
    }
    //delete

    public void delete(Long id)
    {
        if (id == null) throw new RuntimeException("Error");
        movieRepository.deleteById(id);
    }
}
