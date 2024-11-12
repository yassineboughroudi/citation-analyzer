package org.unisannio.model;

import jakarta.persistence.*;


@Entity
@Table(name = "papers")
public class Paper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String doi;
    private String title;
    private String authors;
    private String url;
    private String dblpUrl;
    private Integer citationCount;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    // other getters and setters...

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDblpUrl() {
        return dblpUrl;
    }

    public void setDblpUrl(String dblpUrl) {
        this.dblpUrl = dblpUrl;
    }
    public Integer getCitationCount() {
        return citationCount;
    }

    public void setCitationCount(Integer citationCount) {
        this.citationCount = citationCount;
    }
}
