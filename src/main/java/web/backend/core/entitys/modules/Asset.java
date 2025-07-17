package web.backend.core.entitys.modules;

import jakarta.persistence.*;
import web.backend.core.bases.BaseEntity;

@Entity
@Table(name = "assets")
public class Asset extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "type")
    private String type; // image, audio, video

    @Column(name = "url")
    private String url;

    @Column(name = "filename")
    private String filename;

    public Asset() {}

    public Asset(Project project, String type, String url, String filename) {
        this.project = project;
        this.type = type;
        this.url = url;
        this.filename = filename;
    }

    // Getter & Setter...
}

