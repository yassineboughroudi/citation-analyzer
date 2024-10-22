import org.unisannio.ConferenceCitationToolApplication;
import org.unisannio.service.DblpService;
import org.unisannio.model.Paper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = ConferenceCitationToolApplication.class)
public class DblpServiceTest {

    @Autowired
    private DblpService dblpService;

    @Test
    public void testGetPapersByConference() throws IOException {
        String conferenceName = "icsoc";
        String year = "2020";

        List<Paper> papers = dblpService.getPapersByConference(conferenceName, year);

        assertNotNull(papers, "The papers list should not be null");
        assertFalse(papers.isEmpty(), "The papers list should not be empty");

        // Additional assertions can be added here
        Paper firstPaper = papers.get(0);
        assertNotNull(firstPaper.getTitle(), "Paper title should not be null");
        assertNotNull(firstPaper.getAuthors(), "Paper authors should not be null");
    }
}