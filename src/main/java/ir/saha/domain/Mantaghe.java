package ir.saha.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Mantaghe.
 */
@Entity
@Table(name = "mantaghe")
public class Mantaghe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "mantaghe")
    private Set<Ostan> ostans = new HashSet<>();

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

    public Mantaghe name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Ostan> getOstans() {
        return ostans;
    }

    public Mantaghe ostans(Set<Ostan> ostans) {
        this.ostans = ostans;
        return this;
    }

    public Mantaghe addOstan(Ostan ostan) {
        this.ostans.add(ostan);
        ostan.setMantaghe(this);
        return this;
    }

    public Mantaghe removeOstan(Ostan ostan) {
        this.ostans.remove(ostan);
        ostan.setMantaghe(null);
        return this;
    }

    public void setOstans(Set<Ostan> ostans) {
        this.ostans = ostans;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mantaghe)) {
            return false;
        }
        return id != null && id.equals(((Mantaghe) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Mantaghe{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
