package web.backend.core.entitys.modules;

import jakarta.persistence.*;
import web.backend.core.bases.BaseEntity;

@Entity
@Table(name = "template_slots")
public class TemplateSlot extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(name = "slot_key")
    private String slotKey; // VD: hero_title

    @Column(name = "slot_type")
    private String slotType; // text, image, video, audio

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public TemplateSlot() {}

    public TemplateSlot(Template template, String slotKey, String slotType, String description) {
        this.template = template;
        this.slotKey = slotKey;
        this.slotType = slotType;
        this.description = description;
    }

    // Getter & Setter...
}
