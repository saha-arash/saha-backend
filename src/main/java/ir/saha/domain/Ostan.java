package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ostan.
 */
@Entity
@Table(name = "ostan")
public class Ostan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "ostan")
    private Set<Shahr> shahrs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("ostans")
    private Mantaghe mantaghe;

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

    public Ostan name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Shahr> getShahrs() {
        return shahrs;
    }

    public Ostan shahrs(Set<Shahr> shahrs) {
        this.shahrs = shahrs;
        return this;
    }

    public Ostan addShahr(Shahr shahr) {
        this.shahrs.add(shahr);
        shahr.setOstan(this);
        return this;
    }

    public Ostan removeShahr(Shahr shahr) {
        this.shahrs.remove(shahr);
        shahr.setOstan(null);
        return this;
    }

    public void setShahrs(Set<Shahr> shahrs) {
        this.shahrs = shahrs;
    }

    public Mantaghe getMantaghe() {
        return mantaghe;
    }

    public Ostan mantaghe(Mantaghe mantaghe) {
        this.mantaghe = mantaghe;
        return this;
    }

    public void setMantaghe(Mantaghe mantaghe) {
        this.mantaghe = mantaghe;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ostan)) {
            return false;
        }
        return id != null && id.equals(((Ostan) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ostan{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
