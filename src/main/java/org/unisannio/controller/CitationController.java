package org.unisannio.controller;

import org.unisannio.model.Paper;
import org.unisannio.service.DblpService;
import org.unisannio.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CitationController {

    @Autowired
    private DblpService dblpService;

    @Autowired
    private PaperRepository paperRepository;

    @GetMapping("/conferences/{name}/{year}/papers")
    public List<Paper> getPapersByConference(@PathVariable("name") String conferenceName, @PathVariable("year") String year) throws IOException {
        List<Paper> papers = dblpService.getPapersByConference(conferenceName, year);
        paperRepository.saveAll(papers); // Save papers to the database
        return papers;
    }
}