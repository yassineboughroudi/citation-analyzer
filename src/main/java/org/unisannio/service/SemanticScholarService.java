package org.unisannio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.unisannio.model.Paper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SemanticScholarService {
    private static final Logger logger = LoggerFactory.getLogger(SemanticScholarService.class);
    public static final String API_URL = "https://api.semanticscholar.org/graph/v1/paper/%s?fields=citationCount";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SemanticScholarService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public Integer getCitationCount(Paper paper) {
        String doi = paper.getDoi();
        String title = paper.getTitle();

        logger.info("Fetching citation count for paper: DOI = {}, Title = {}", doi, title);

        try {
            if (doi != null && !doi.isEmpty()) {
                Integer citationCount = getCitationCountByDoi(doi);
                if (citationCount != null) {
                    logger.info("Citation count obtained via DOI: {}", citationCount);
                    return citationCount;
                } else {
                    logger.warn("Citation count not found via DOI for paper: {}", doi);
                }
            }

            // Proceed to title lookup only if title is not null
            if (title != null && !title.isEmpty()) {
                Integer citationCount = getCitationCountByTitle(title);
                logger.info("Citation count obtained via Title: {}", citationCount);
                return citationCount;
            } else {
                logger.error("Title is null or empty, cannot proceed with title lookup.");
            }
        } catch (Exception e) {
            logger.error("Error fetching citation count for paper: DOI = {}, Title = {}", doi, title, e);
        }

        return null;
    }

    private Integer getCitationCountByDoi(String doi) throws JsonProcessingException {
        try {
            String encodedDoi = URLEncoder.encode(doi, StandardCharsets.UTF_8);
            String url = String.format(API_URL, "DOI:" + encodedDoi);

            String response = restTemplate.getForObject(url, String.class);

            JsonNode rootNode = objectMapper.readTree(response);
            if (rootNode.has("citationCount")) {
                return rootNode.get("citationCount").asInt();
            } else {
                // Log that citation count is not available
                System.err.println("Citation count not found in response for DOI: " + doi);
            }
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error when fetching citation count for DOI {}: {}", doi, e.getStatusCode());
            throw e; // Rethrow or handle accordingly
        } catch (Exception e) {
            logger.error("Unexpected error when fetching citation count for DOI {}: {}", doi, e.getMessage(), e);
            throw e; // Rethrow or handle accordingly
        }
        return null;
    }



    public Integer getCitationCountByTitle(String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String url = String.format(API_URL, encodedTitle);

            // Send API request
            String response = restTemplate.getForObject(url, String.class);

            // Parse the response JSON
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataArray = rootNode.path("data");

            // Iterate through the returned papers
            for (JsonNode node : dataArray) {
                String retrievedTitle = node.path("title").asText();

                // Normalize both titles before comparison
                String normalizedRetrievedTitle = normalizeTitle(retrievedTitle);
                String normalizedInputTitle = normalizeTitle(title);

                if (normalizedRetrievedTitle.equals(normalizedInputTitle)) {
                    return node.path("citationCount").asInt();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }

        return null;  // Return null if no match found
    }

    // Method to normalize a title (removes punctuation, converts to lowercase, trims spaces)
    private String normalizeTitle(String title) {
        return title.replaceAll("[^a-zA-Z0-9\\s]", "")  // Remove all non-alphanumeric and non-space characters
                .toLowerCase()                     // Convert to lowercase
                .trim();                           // Remove leading/trailing spaces
    }
}
