package web.backend.core.entitys.modules;

import jakarta.persistence.*;
import web.backend.core.bases.BaseEntity;

@Entity
@Table(name = "templates")
public class Template extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    public Template() {
    }

    public Template(String name, String description, Double price , String thumbnailUrl) {
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.price = price;
    }

    // Getter & Setter...
}
