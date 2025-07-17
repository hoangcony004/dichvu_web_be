package web.backend.core.entitys.modules;

import jakarta.persistence.*;
import web.backend.core.bases.BaseEntity;
import web.backend.core.entitys.systems.SysUser;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private SysUser user;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "status")
    private String status; // draft / published

    public Project() {}

    public Project(SysUser user, Template template, String title, String slug, String status) {
        this.user = user;
        this.template = template;
        this.title = title;
        this.slug = slug;
        this.status = status;
    }

    // Getter & Setter...
}
