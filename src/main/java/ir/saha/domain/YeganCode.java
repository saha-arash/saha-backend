package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A YeganCode.
 */
@Entity
@Table(name = "yegan_code")
public class YeganCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @OneToOne
    @JoinColumn(unique = true)
    private Yegan yegan;

    @OneToMany(mappedBy = "yeganCode")
    private Set<Karbar> karbars = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("yeganCodes")
    private NirooCode nirooCode;

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

    public YeganCode name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public YeganCode code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Yegan getYegan() {
        return yegan;
    }

    public YeganCode yegan(Yegan yegan) {
        this.yegan = yegan;
        return this;
    }

    public void setYegan(Yegan yegan) {
        this.yegan = yegan;
    }

    public Set<Karbar> getKarbars() {
        return karbars;
    }

    public YeganCode karbars(Set<Karbar> karbars) {
        this.karbars = karbars;
        return this;
    }

    public YeganCode addKarbar(Karbar karbar) {
        this.karbars.add(karbar);
        karbar.setYeganCode(this);
        return this;
    }

    public YeganCode removeKarbar(Karbar karbar) {
        this.karbars.remove(karbar);
        karbar.setYeganCode(null);
        return this;
    }

    public void setKarbars(Set<Karbar> karbars) {
        this.karbars = karbars;
    }

    public NirooCode getNirooCode() {
        return nirooCode;
    }

    public YeganCode nirooCode(NirooCode nirooCode) {
        this.nirooCode = nirooCode;
        return this;
    }

    public void setNirooCode(NirooCode nirooCode) {
        this.nirooCode = nirooCode;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof YeganCode)) {
            return false;
        }
        return id != null && id.equals(((YeganCode) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "YeganCode{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
