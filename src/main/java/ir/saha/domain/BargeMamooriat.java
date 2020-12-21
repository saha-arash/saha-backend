package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import ir.saha.domain.enumeration.VaziatBargeMamooriat;

/**
 * A BargeMamooriat.
 */
@Entity
@Table(name = "barge_mamooriat")
public class BargeMamooriat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaziat")
    private VaziatBargeMamooriat vaziat;

    @Column(name = "sale_mamooriat")
    private Integer saleMamooriat;

    @Column(name = "shoroo_mamooriat")
    private Instant shorooMamooriat;

    @Column(name = "payan_mamooriat")
    private Instant payanMamooriat;

    @OneToMany(mappedBy = "bargeMamooriat")
    private Set<FileBargeMamooriat> madareks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("sarparestemamooriats")
    private Karbar sarparast;

    @ManyToOne
    @JsonIgnoreProperties("bargeMamoorits")
    private Yegan yegan;

    @ManyToOne
    @JsonIgnoreProperties("bargeMamooriats")
    private HesabResi hesabResi;

    @ManyToMany(mappedBy = "bargeMamoorits")
    @JsonIgnore
    private Set<Karbar> nafars = new HashSet<>();

    @ManyToMany(mappedBy = "binanadeBargeMamoorits")
    @JsonIgnore
    private Set<Karbar> binandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaziatBargeMamooriat getVaziat() {
        return vaziat;
    }

    public BargeMamooriat vaziat(VaziatBargeMamooriat vaziat) {
        this.vaziat = vaziat;
        return this;
    }

    public void setVaziat(VaziatBargeMamooriat vaziat) {
        this.vaziat = vaziat;
    }

    public Integer getSaleMamooriat() {
        return saleMamooriat;
    }

    public BargeMamooriat saleMamooriat(Integer saleMamooriat) {
        this.saleMamooriat = saleMamooriat;
        return this;
    }

    public void setSaleMamooriat(Integer saleMamooriat) {
        this.saleMamooriat = saleMamooriat;
    }

    public Instant getShorooMamooriat() {
        return shorooMamooriat;
    }

    public BargeMamooriat shorooMamooriat(Instant shorooMamooriat) {
        this.shorooMamooriat = shorooMamooriat;
        return this;
    }

    public void setShorooMamooriat(Instant shorooMamooriat) {
        this.shorooMamooriat = shorooMamooriat;
    }

    public Instant getPayanMamooriat() {
        return payanMamooriat;
    }

    public BargeMamooriat payanMamooriat(Instant payanMamooriat) {
        this.payanMamooriat = payanMamooriat;
        return this;
    }

    public void setPayanMamooriat(Instant payanMamooriat) {
        this.payanMamooriat = payanMamooriat;
    }

    public Set<FileBargeMamooriat> getMadareks() {
        return madareks;
    }

    public BargeMamooriat madareks(Set<FileBargeMamooriat> fileBargeMamooriats) {
        this.madareks = fileBargeMamooriats;
        return this;
    }

    public BargeMamooriat addMadarek(FileBargeMamooriat fileBargeMamooriat) {
        this.madareks.add(fileBargeMamooriat);
        fileBargeMamooriat.setBargeMamooriat(this);
        return this;
    }

    public BargeMamooriat removeMadarek(FileBargeMamooriat fileBargeMamooriat) {
        this.madareks.remove(fileBargeMamooriat);
        fileBargeMamooriat.setBargeMamooriat(null);
        return this;
    }

    public void setMadareks(Set<FileBargeMamooriat> fileBargeMamooriats) {
        this.madareks = fileBargeMamooriats;
    }

    public Karbar getSarparast() {
        return sarparast;
    }

    public BargeMamooriat sarparast(Karbar karbar) {
        this.sarparast = karbar;
        return this;
    }

    public void setSarparast(Karbar karbar) {
        this.sarparast = karbar;
    }

    public Yegan getYegan() {
        return yegan;
    }

    public BargeMamooriat yegan(Yegan yegan) {
        this.yegan = yegan;
        return this;
    }

    public void setYegan(Yegan yegan) {
        this.yegan = yegan;
    }

    public HesabResi getHesabResi() {
        return hesabResi;
    }

    public BargeMamooriat hesabResi(HesabResi hesabResi) {
        this.hesabResi = hesabResi;
        return this;
    }

    public void setHesabResi(HesabResi hesabResi) {
        this.hesabResi = hesabResi;
    }

    public Set<Karbar> getNafars() {
        return nafars;
    }

    public BargeMamooriat nafars(Set<Karbar> karbars) {
        this.nafars = karbars;
        return this;
    }

    public BargeMamooriat addNafar(Karbar karbar) {
        this.nafars.add(karbar);
        karbar.getBargeMamoorits().add(this);
        return this;
    }

    public BargeMamooriat removeNafar(Karbar karbar) {
        this.nafars.remove(karbar);
        karbar.getBargeMamoorits().remove(this);
        return this;
    }

    public void setNafars(Set<Karbar> karbars) {
        this.nafars = karbars;
    }

    public Set<Karbar> getBinandes() {
        return binandes;
    }

    public BargeMamooriat binandes(Set<Karbar> karbars) {
        this.binandes = karbars;
        return this;
    }

    public BargeMamooriat addBinande(Karbar karbar) {
        this.binandes.add(karbar);
        karbar.getBinanadeBargeMamoorits().add(this);
        return this;
    }

    public BargeMamooriat removeBinande(Karbar karbar) {
        this.binandes.remove(karbar);
        karbar.getBinanadeBargeMamoorits().remove(this);
        return this;
    }

    public void setBinandes(Set<Karbar> karbars) {
        this.binandes = karbars;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BargeMamooriat)) {
            return false;
        }
        return id != null && id.equals(((BargeMamooriat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BargeMamooriat{" +
            "id=" + getId() +
            ", vaziat='" + getVaziat() + "'" +
            ", saleMamooriat=" + getSaleMamooriat() +
            ", shorooMamooriat='" + getShorooMamooriat() + "'" +
            ", payanMamooriat='" + getPayanMamooriat() + "'" +
            "}";
    }
}
