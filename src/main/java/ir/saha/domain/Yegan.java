package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Yegan.
 */
@Entity
@Table(name = "yegan")
public class Yegan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "yegan")
    private Set<Karbar> karbars = new HashSet<>();

    @OneToMany(mappedBy = "yeganErsalKonanade")
    private Set<Payam> sandoghVoroodis = new HashSet<>();

    @OneToMany(mappedBy = "yeganDaryaftKonanade")
    private Set<Payam> snadoghKhoroojis = new HashSet<>();

    @OneToMany(mappedBy = "yegan")
    private Set<BargeMamooriat> bargeMamoorits = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "yegan_zir_yegan",
               joinColumns = @JoinColumn(name = "yegan_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "zir_yegan_id", referencedColumnName = "id"))
    private Set<Yegan> zirYegans = new HashSet<>();

    @OneToOne(mappedBy = "yegan")
    @JsonIgnore
    private YeganCode yeganCode;

    @ManyToOne
    @JsonIgnoreProperties("yegans")
    private NirooCode nirooCode;

    @ManyToOne
    @JsonIgnoreProperties("yegans")
    private Shahr shahr;

    @ManyToOne
    @JsonIgnoreProperties("yegans")
    private YeganType yeganType;

    @ManyToMany(mappedBy = "zirYegans")
    @JsonIgnore
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

    public Yegan name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public Yegan code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Karbar> getKarbars() {
        return karbars;
    }

    public Yegan karbars(Set<Karbar> karbars) {
        this.karbars = karbars;
        return this;
    }

    public Yegan addKarbar(Karbar karbar) {
        this.karbars.add(karbar);
        karbar.setYegan(this);
        return this;
    }

    public Yegan removeKarbar(Karbar karbar) {
        this.karbars.remove(karbar);
        karbar.setYegan(null);
        return this;
    }

    public void setKarbars(Set<Karbar> karbars) {
        this.karbars = karbars;
    }

    public Set<Payam> getSandoghVoroodis() {
        return sandoghVoroodis;
    }

    public Yegan sandoghVoroodis(Set<Payam> payams) {
        this.sandoghVoroodis = payams;
        return this;
    }

    public Yegan addSandoghVoroodi(Payam payam) {
        this.sandoghVoroodis.add(payam);
        payam.setYeganErsalKonanade(this);
        return this;
    }

    public Yegan removeSandoghVoroodi(Payam payam) {
        this.sandoghVoroodis.remove(payam);
        payam.setYeganErsalKonanade(null);
        return this;
    }

    public void setSandoghVoroodis(Set<Payam> payams) {
        this.sandoghVoroodis = payams;
    }

    public Set<Payam> getSnadoghKhoroojis() {
        return snadoghKhoroojis;
    }

    public Yegan snadoghKhoroojis(Set<Payam> payams) {
        this.snadoghKhoroojis = payams;
        return this;
    }

    public Yegan addSnadoghKhorooji(Payam payam) {
        this.snadoghKhoroojis.add(payam);
        payam.setYeganDaryaftKonanade(this);
        return this;
    }

    public Yegan removeSnadoghKhorooji(Payam payam) {
        this.snadoghKhoroojis.remove(payam);
        payam.setYeganDaryaftKonanade(null);
        return this;
    }

    public void setSnadoghKhoroojis(Set<Payam> payams) {
        this.snadoghKhoroojis = payams;
    }

    public Set<BargeMamooriat> getBargeMamoorits() {
        return bargeMamoorits;
    }

    public Yegan bargeMamoorits(Set<BargeMamooriat> bargeMamooriats) {
        this.bargeMamoorits = bargeMamooriats;
        return this;
    }

    public Yegan addBargeMamoorit(BargeMamooriat bargeMamooriat) {
        this.bargeMamoorits.add(bargeMamooriat);
        bargeMamooriat.setYegan(this);
        return this;
    }

    public Yegan removeBargeMamoorit(BargeMamooriat bargeMamooriat) {
        this.bargeMamoorits.remove(bargeMamooriat);
        bargeMamooriat.setYegan(null);
        return this;
    }

    public void setBargeMamoorits(Set<BargeMamooriat> bargeMamooriats) {
        this.bargeMamoorits = bargeMamooriats;
    }

    public Set<Yegan> getZirYegans() {
        return zirYegans;
    }

    public Yegan zirYegans(Set<Yegan> yegans) {
        this.zirYegans = yegans;
        return this;
    }

    public Yegan addZirYegan(Yegan yegan) {
        this.zirYegans.add(yegan);
        yegan.getYegans().add(this);
        return this;
    }

    public Yegan removeZirYegan(Yegan yegan) {
        this.zirYegans.remove(yegan);
        yegan.getYegans().remove(this);
        return this;
    }

    public void setZirYegans(Set<Yegan> yegans) {
        this.zirYegans = yegans;
    }

    public YeganCode getYeganCode() {
        return yeganCode;
    }

    public Yegan yeganCode(YeganCode yeganCode) {
        this.yeganCode = yeganCode;
        return this;
    }

    public void setYeganCode(YeganCode yeganCode) {
        this.yeganCode = yeganCode;
    }

    public NirooCode getNirooCode() {
        return nirooCode;
    }

    public Yegan nirooCode(NirooCode nirooCode) {
        this.nirooCode = nirooCode;
        return this;
    }

    public void setNirooCode(NirooCode nirooCode) {
        this.nirooCode = nirooCode;
    }

    public Shahr getShahr() {
        return shahr;
    }

    public Yegan shahr(Shahr shahr) {
        this.shahr = shahr;
        return this;
    }

    public void setShahr(Shahr shahr) {
        this.shahr = shahr;
    }

    public YeganType getYeganType() {
        return yeganType;
    }

    public Yegan yeganType(YeganType yeganType) {
        this.yeganType = yeganType;
        return this;
    }

    public void setYeganType(YeganType yeganType) {
        this.yeganType = yeganType;
    }

    public Set<Yegan> getYegans() {
        return yegans;
    }

    public Yegan yegans(Set<Yegan> yegans) {
        this.yegans = yegans;
        return this;
    }

    public Yegan addYegan(Yegan yegan) {
        this.yegans.add(yegan);
        yegan.getZirYegans().add(this);
        return this;
    }

    public Yegan removeYegan(Yegan yegan) {
        this.yegans.remove(yegan);
        yegan.getZirYegans().remove(this);
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
        if (!(o instanceof Yegan)) {
            return false;
        }
        return id != null && id.equals(((Yegan) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Yegan{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
