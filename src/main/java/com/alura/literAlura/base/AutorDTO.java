package com.alura.literAlura.base;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorDTO(

   @JsonAlias("birth_year") Integer anioNacimiento,
   @JsonAlias("death_year") Integer fechaMuerte,
   @JsonAlias("name") String nombre){
}
