package ir.saha.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A YeganType.
 */
@Entity
@Table(name = "yegan_type")
public class YeganType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "yeganType")
    private Set<Yegan> yegans = new HashSet<>();

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

    public YeganType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Yegan> getYegans() {
        return yegans;
    }

    public YeganType yegans(Set<Yegan> yegans) {
        this.yegans = yegans;
        return this;
    }

    public YeganType addYegan(Yegan yegan) {
        this.yegans.add(yegan);
        yegan.setYeganType(this);
        return this;
    }

    public YeganType removeYegan(Yegan yegan) {
        this.yegans.remove(yegan);
        yegan.setYeganType(null);
        return this;
    }

    public void setYegans(Set<Yegan> yegans) {
        this.yegans = yegans;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof YeganType)) {
            return false;
        }
        return id != null && id.equals(((YeganType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "YeganType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
