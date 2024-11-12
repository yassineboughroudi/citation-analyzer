import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;
import org.unisannio.ConferenceCitationToolApplication;
import org.unisannio.model.Paper;
import org.unisannio.service.SemanticScholarService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ConferenceCitationToolApplication.class)
public class SemanticScholarServiceTest {

    @Autowired
    private SemanticScholarService semanticScholarService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testGetCitationCount_Success() throws Exception {
        // Arrange
        Paper paper = new Paper();
        paper.setTitle("Deep Learning");
        String responseJson = "{ \"data\": [ { \"title\": \"Deep Learning\", \"citationCount\": 1000 } ] }";

        String encodedTitle = java.net.URLEncoder.encode(paper.getTitle(), java.nio.charset.StandardCharsets.UTF_8);
        String url = String.format(SemanticScholarService.API_URL, encodedTitle);

        when(restTemplate.getForObject(url, String.class)).thenReturn(responseJson);

        // Act
        Integer citationCount = semanticScholarService.getCitationCount(paper);

        // Assert
        assertNotNull(citationCount);
        assertEquals(1000, citationCount);
    }

    @Test
    public void testGetCitationCount_NoMatch() throws Exception {
        // Arrange
        Paper paper = new Paper();
        paper.setTitle("Nonexistent Paper");
        String responseJson = "{ \"data\": [] }";

        String encodedTitle = java.net.URLEncoder.encode(paper.getTitle(), java.nio.charset.StandardCharsets.UTF_8);
        String url = String.format(SemanticScholarService.API_URL, encodedTitle);

        when(restTemplate.getForObject(url, String.class)).thenReturn(responseJson);

        // Act
        Integer citationCount = semanticScholarService.getCitationCount(paper);

        // Assert
        assertNull(citationCount);
    }
/*
    @Test
    public void testGetCitationCount_Exception() throws Exception {
        // Arrange
        Paper paper = new Paper();
        paper.setTitle("Some Paper");

        String encodedTitle = java.net.URLEncoder.encode(paper.getTitle(), java.nio.charset.StandardCharsets.UTF_8);
        String url = String.format(SemanticScholarService.API_URL, encodedTitle);

        when(restTemplate.getForObject(url, String.class)).thenThrow(new RuntimeException("API Error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            semanticScholarService.getCitationCount(paper);
        });

        assertEquals("API Error", exception.getMessage());
    }

 */
}
