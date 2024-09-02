package com.bharath.firstspringproject.controller;

import com.bharath.firstspringproject.model.Movie;
import com.bharath.firstspringproject.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    //create

    @BeforeEach

    void cleanUp(){
        movieRepository.deleteAllInBatch();
    }

    @Autowired
    MovieRepository movieRepository;

    @Test
    void givenMovie_whenCreated_returnedMovie() throws Exception{

        //givenMovie
        Movie movie = new Movie();
        movie.setName("Vasantham");
        movie.setDirector("RBChowdary");
        movie.setActors(List.of("Venkatesh", "Kalyani", "Arthi"));

        //whenCreated
        var resultActions = mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        //returnedmovie
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect((jsonPath("$.director", is(movie.getDirector()))))
                .andExpect(jsonPath("$.actors", is((movie.getActors()))));
    }

    @Test
    void givenGetMovie_whenfetched_returnFetchedMovie() throws Exception {

        //givenGetMovie
        Movie movie = new Movie();
        movie.setName("Vasantham");
        movie.setDirector("RBChowdary");
        movie.setActors(List.of("Venkatesh", "Kalyani", "Arthi"));
        Movie savedMovie = movieRepository.save(movie);

        //whenfetched
        var resultActions = mockMvc.perform(get("/movies/" + savedMovie.getId()));

        //returnFetchedMovie
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedMovie.getId().intValue())))
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect((jsonPath("$.director", is(movie.getDirector()))))
                .andExpect(jsonPath("$.actors", is((movie.getActors()))));

    }

    @Test
    void givenUpdateMovie_whenUpdated_returnUpdatedMovie() throws Exception {

        //givenGetMovie
        Movie movie = new Movie();
        movie.setName("Vasantham");
        movie.setDirector("RBChowdary");
        movie.setActors(List.of("Venkatesh", "Kalyani", "Arthi"));
        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();
        movie.setActors(List.of("Venkatesh", "Kalyani", "Arthi","SA Rajkumar"));

        var movieUpdated = mockMvc.perform(put("/movies/" + id)
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(objectMapper.writeValueAsString(movie)));
        movieUpdated.andDo(print())
                .andExpect(status().isOk());
        var fetchedMovies = mockMvc.perform(get("/movies/" + id));
        fetchedMovies.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect((jsonPath("$.director", is(movie.getDirector()))))
                .andExpect(jsonPath("$.actors", is((movie.getActors()))));

    }

    @Test
    void givenDeleteMovie_whendeleted_returnDeletedMovie() throws Exception {

        //givenMovie
        Movie movie = new Movie();
        movie.setName("Vasantham");
        movie.setDirector("RBChowdary");
        movie.setActors(List.of("Venkatesh", "Kalyani", "Arthi"));
        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();

        var deleteMovie = mockMvc.perform(delete("/movies/" + id));
        deleteMovie.andDo(print())
                .andExpect(status().isOk());

        assertFalse(movieRepository.findById(id).isPresent());

    }
}