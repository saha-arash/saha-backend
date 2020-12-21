package ir.saha.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Semat.
 */
@Entity
@Table(name = "semat")
public class Semat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "onvan_shoghli")
    private String onvanShoghli;

    @OneToMany(mappedBy = "semat")
    private Set<Karbar> karbars = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOnvanShoghli() {
        return onvanShoghli;
    }

    public Semat onvanShoghli(String onvanShoghli) {
        this.onvanShoghli = onvanShoghli;
        return this;
    }

    public void setOnvanShoghli(String onvanShoghli) {
        this.onvanShoghli = onvanShoghli;
    }

    public Set<Karbar> getKarbars() {
        return karbars;
    }

    public Semat karbars(Set<Karbar> karbars) {
        this.karbars = karbars;
        return this;
    }

    public Semat addKarbar(Karbar karbar) {
        this.karbars.add(karbar);
        karbar.setSemat(this);
        return this;
    }

    public Semat removeKarbar(Karbar karbar) {
        this.karbars.remove(karbar);
        karbar.setSemat(null);
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
        if (!(o instanceof Semat)) {
            return false;
        }
        return id != null && id.equals(((Semat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Semat{" +
            "id=" + getId() +
            ", onvanShoghli='" + getOnvanShoghli() + "'" +
            "}";
    }
}
