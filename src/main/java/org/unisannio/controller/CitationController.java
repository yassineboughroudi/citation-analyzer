package org.unisannio.controller;

import org.unisannio.model.Paper;
import org.unisannio.service.DblpService;
import org.unisannio.service.SemanticScholarService;
import org.unisannio.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.common.util.concurrent.RateLimiter;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/conferences")
public class CitationController {

    @Autowired
    private DblpService dblpService;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private SemanticScholarService semanticScholarService;

    // Create a RateLimiter that allows one request every 3 seconds
    private final RateLimiter rateLimiter = RateLimiter.create(1.0 / 4.0);
    @GetMapping("/conferences/{name}/{year}/papers")
    public List<Paper> getPapersByConference(@PathVariable("name") String conferenceName, @PathVariable("year") String year) throws IOException {
        List<Paper> papers = dblpService.getPapersByConference(conferenceName, year);
        paperRepository.saveAll(papers); // Save papers to the database
        return papers;
    }

    @GetMapping("/{conferenceName}/{year}/papers-with-citations")
    public List<Paper> getPapersWithCitations(
            @PathVariable("conferenceName") String conferenceName,
            @PathVariable("year") String year) throws IOException {

        List<Paper> papers = dblpService.getPapersByConference(conferenceName, year);

        for (Paper paper : papers) {
            rateLimiter.acquire(); // Acquires a permit before proceeding

            Integer citationCount = semanticScholarService.getCitationCount(paper);
            paper.setCitationCount(citationCount != null ? citationCount : 0);
        }

        return papers;
    }

}
