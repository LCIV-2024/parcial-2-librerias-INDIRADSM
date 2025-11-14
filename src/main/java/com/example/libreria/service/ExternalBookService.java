package com.example.libreria.service;

import com.example.libreria.dto.ExternalBookDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ExternalBookService {

    private final RestTemplate restTemplate;

    @Value("${external.api.books.url}")
    private String externalApiUrl;

    public ExternalBookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ExternalBookDTO> fetchAllBooks() {
        try {
            log.info("Fetching books from external API: {}", externalApiUrl);

            ResponseEntity<List<ExternalBookDTO>> response = restTemplate.exchange(
                    externalApiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ExternalBookDTO>>() {}
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyList();

        } catch (RestClientException e) {
            log.error("Error fetching books: {}", e.getMessage());
            throw new RuntimeException("Error al obtener libros externos", e);
        }
    }

    public ExternalBookDTO fetchBookById(Long id) {
        try {
            String url = externalApiUrl + "/" + id;
            ExternalBookDTO book = restTemplate.getForObject(url, ExternalBookDTO.class);

            if (book == null)
                throw new RuntimeException("Libro no encontrado en API externa");

            return book;

        } catch (RestClientException e) {
            log.error("Error fetching book {}: {}", id, e.getMessage());
            throw new RuntimeException("Error consultando API externa", e);
        }
    }
}

