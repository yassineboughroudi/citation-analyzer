package org.unisannio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.unisannio.model.Paper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class DblpService {
    private static final Logger logger = LoggerFactory.getLogger(DblpService.class);
    private static final String DBLP_API_URL = "https://dblp.org/search/publ/api?q=";

    public List<Paper> getPapersByConference(String conferenceName, String year) throws IOException {
        String query = String.format("conf/%s/%s", conferenceName, year);
        String url = DBLP_API_URL + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&format=json";

        List<Paper> papers = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(entity.getContent());
                    JsonNode hits = rootNode.path("result").path("hits").path("hit");

                    for (JsonNode hit : hits) {
                        JsonNode info = hit.path("info");
                        String title = info.path("title").asText("");
                        String urlLink = info.path("ee").asText("");
                        String dblpUrl = info.path("url").asText("");

// Authors extraction
                        JsonNode authorsNode = info.path("authors").path("author");
                        List<String> authorsList = new ArrayList<>();

                        if (authorsNode.isMissingNode() || authorsNode.isNull()) {
                            // Handle missing authors
                            authorsList.add("Unknown");
                        } else if (authorsNode.isArray()) {
                            for (JsonNode authorNode : authorsNode) {
                                String authorName = authorNode.path("text").asText("").trim();  // Extract the 'text' field
                                if (!authorName.isEmpty()) {
                                    authorsList.add(authorName);
                                }
                            }
                        } else if (authorsNode.has("text")) {
                            // Handle case where there is only one author as a single object
                            String authorName = authorsNode.path("text").asText("").trim();
                            if (!authorName.isEmpty()) {
                                authorsList.add(authorName);
                            }
                        } else {
                            authorsList.add("Unknown");
                        }

                        if (authorsList.isEmpty()) {
                            authorsList.add("Unknown");
                        }

// Create a Paper object
                        Paper paper = new Paper();
                        paper.setTitle(title);
                        paper.setAuthors(String.join(", ", authorsList));
                        paper.setUrl(urlLink);
                        paper.setDblpUrl(dblpUrl);

                        papers.add(paper);

                    }
                }
            }
        }

        return papers;
    }

}