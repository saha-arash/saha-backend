package ir.saha.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A NirooCode.
 */
@Entity
@Table(name = "niroo_code")
public class NirooCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "nirooCode")
    private Set<YeganCode> yeganCodes = new HashSet<>();

    @OneToMany(mappedBy = "nirooCode")
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

    public NirooCode name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public NirooCode code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<YeganCode> getYeganCodes() {
        return yeganCodes;
    }

    public NirooCode yeganCodes(Set<YeganCode> yeganCodes) {
        this.yeganCodes = yeganCodes;
        return this;
    }

    public NirooCode addYeganCode(YeganCode yeganCode) {
        this.yeganCodes.add(yeganCode);
        yeganCode.setNirooCode(this);
        return this;
    }

    public NirooCode removeYeganCode(YeganCode yeganCode) {
        this.yeganCodes.remove(yeganCode);
        yeganCode.setNirooCode(null);
        return this;
    }

    public void setYeganCodes(Set<YeganCode> yeganCodes) {
        this.yeganCodes = yeganCodes;
    }

    public Set<Yegan> getYegans() {
        return yegans;
    }

    public NirooCode yegans(Set<Yegan> yegans) {
        this.yegans = yegans;
        return this;
    }

    public NirooCode addYegan(Yegan yegan) {
        this.yegans.add(yegan);
        yegan.setNirooCode(this);
        return this;
    }

    public NirooCode removeYegan(Yegan yegan) {
        this.yegans.remove(yegan);
        yegan.setNirooCode(null);
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
        if (!(o instanceof NirooCode)) {
            return false;
        }
        return id != null && id.equals(((NirooCode) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "NirooCode{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
