package ir.saha.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Daraje.
 */
@Entity
@Table(name = "daraje")
public class Daraje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "daraje")
    private Set<Karbar> karbars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Daraje name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Daraje description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Karbar> getKarbars() {
        return karbars;
    }

    public Daraje karbars(Set<Karbar> karbars) {
        this.karbars = karbars;
        return this;
    }

    public Daraje addKarbar(Karbar karbar) {
        this.karbars.add(karbar);
        karbar.setDaraje(this);
        return this;
    }

    public Daraje removeKarbar(Karbar karbar) {
        this.karbars.remove(karbar);
        karbar.setDaraje(null);
        return this;
    }

    public void setKarbars(Set<Karbar> karbars) {
        this.karbars = karbars;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Daraje)) {
            return false;
        }
        return id != null && id.equals(((Daraje) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Daraje{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
