package com.alura.literAlura.model;

import com.alura.literAlura.base.LibroDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatoLibro(
       @JsonAlias("results") List<LibroDTO> results
) {
}
