package web.backend.core.entitys.modules;

import jakarta.persistence.*;
import web.backend.core.bases.BaseEntity;

@Entity
@Table(name = "project_contents")
public class ProjectContent extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "key")
    private String key; // VD: hero_title

    @Column(name = "value", columnDefinition = "TEXT")
    private String value; // VD: Trung & Mai

    public ProjectContent() {}

    public ProjectContent(Project project, String key, String value) {
        this.project = project;
        this.key = key;
        this.value = value;
    }

    // Getter & Setter...
}
