import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.unisannio.ConferenceCitationToolApplication;
import org.unisannio.model.Paper;
import org.unisannio.service.SemanticScholarService;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ConferenceCitationToolApplication.class)
public class SemanticScholarServiceIntegrationTest {

    @Autowired
    private SemanticScholarService semanticScholarService;
    /*
    @Test
    public void testGetCitationCount_UsingDoi() {
        // Arrange
        Paper paper = new Paper();
        paper.setDoi("10.1109/ECRIME.2014.6963160");

        // Act
        Integer citationCount = semanticScholarService.getCitationCount(paper);

        // Assert
        assertNotNull(citationCount);
        assertTrue(citationCount >= 0); // Citation counts can be zero
    }
    */
}
