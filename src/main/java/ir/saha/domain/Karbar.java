package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Karbar.
 */
@Entity
@Table(name = "karbar")
public class Karbar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "shoghl_sazmani")
    private String shoghlSazmani;

    @Column(name = "shoghl_amali")
    private String shoghlAmali;

    @Column(name = "code_perseneli")
    private String codePerseneli;

    @Column(name = "bezaneshate")
    private Boolean bezaneshate;

    @Column(name = "sazmani")
    private Boolean sazmani;

    @Column(name = "tarikh_bazneshastegi")
    private Instant tarikhBazneshastegi;

    @Column(name = "tarikh_estekhdam")
    private Instant tarikhEstekhdam;

    @OneToMany(mappedBy = "karbar")
    private Set<Morkhasi> morkhasis = new HashSet<>();

    @OneToMany(mappedBy = "karbar")
    private Set<Dore> dores = new HashSet<>();

    @OneToMany(mappedBy = "karbar")
    private Set<Negahbani> negahbanis = new HashSet<>();

    @OneToMany(mappedBy = "sarparast")
    private Set<BargeMamooriat> sarparestemamooriats = new HashSet<>();

    @OneToMany(mappedBy = "karbarErsalKonande")
    private Set<Payam> sandoghVoroodis = new HashSet<>();

    @OneToMany(mappedBy = "karbarDaryaftKonand")
    private Set<Payam> snadoghKhoroojis = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "karbar_barge_mamoorit",
               joinColumns = @JoinColumn(name = "karbar_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "barge_mamoorit_id", referencedColumnName = "id"))
    private Set<BargeMamooriat> bargeMamoorits = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "karbar_binanade_barge_mamoorit",
               joinColumns = @JoinColumn(name = "karbar_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "binanade_barge_mamoorit_id", referencedColumnName = "id"))
    private Set<BargeMamooriat> binanadeBargeMamoorits = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("karbars")
    private Yegan yegan;

    @ManyToOne
    @JsonIgnoreProperties("karbars")
    private YeganCode yeganCode;

    @ManyToOne
    @JsonIgnoreProperties("karbars")
    private Daraje daraje;

    @ManyToOne
    @JsonIgnoreProperties("karbars")
    private Semat semat;

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

    public Karbar name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShoghlSazmani() {
        return shoghlSazmani;
    }

    public Karbar shoghlSazmani(String shoghlSazmani) {
        this.shoghlSazmani = shoghlSazmani;
        return this;
    }

    public void setShoghlSazmani(String shoghlSazmani) {
        this.shoghlSazmani = shoghlSazmani;
    }

    public String getShoghlAmali() {
        return shoghlAmali;
    }

    public Karbar shoghlAmali(String shoghlAmali) {
        this.shoghlAmali = shoghlAmali;
        return this;
    }

    public void setShoghlAmali(String shoghlAmali) {
        this.shoghlAmali = shoghlAmali;
    }

    public String getCodePerseneli() {
        return codePerseneli;
    }

    public Karbar codePerseneli(String codePerseneli) {
        this.codePerseneli = codePerseneli;
        return this;
    }

    public void setCodePerseneli(String codePerseneli) {
        this.codePerseneli = codePerseneli;
    }

    public Boolean isBezaneshate() {
        return bezaneshate;
    }

    public Karbar bezaneshate(Boolean bezaneshate) {
        this.bezaneshate = bezaneshate;
        return this;
    }

    public void setBezaneshate(Boolean bezaneshate) {
        this.bezaneshate = bezaneshate;
    }

    public Boolean isSazmani() {
        return sazmani;
    }

    public Karbar sazmani(Boolean sazmani) {
        this.sazmani = sazmani;
        return this;
    }

    public void setSazmani(Boolean sazmani) {
        this.sazmani = sazmani;
    }

    public Instant getTarikhBazneshastegi() {
        return tarikhBazneshastegi;
    }

    public Karbar tarikhBazneshastegi(Instant tarikhBazneshastegi) {
        this.tarikhBazneshastegi = tarikhBazneshastegi;
        return this;
    }

    public void setTarikhBazneshastegi(Instant tarikhBazneshastegi) {
        this.tarikhBazneshastegi = tarikhBazneshastegi;
    }

    public Instant getTarikhEstekhdam() {
        return tarikhEstekhdam;
    }

    public Karbar tarikhEstekhdam(Instant tarikhEstekhdam) {
        this.tarikhEstekhdam = tarikhEstekhdam;
        return this;
    }

    public void setTarikhEstekhdam(Instant tarikhEstekhdam) {
        this.tarikhEstekhdam = tarikhEstekhdam;
    }

    public Set<Morkhasi> getMorkhasis() {
        return morkhasis;
    }

    public Karbar morkhasis(Set<Morkhasi> morkhasis) {
        this.morkhasis = morkhasis;
        return this;
    }

    public Karbar addMorkhasi(Morkhasi morkhasi) {
        this.morkhasis.add(morkhasi);
        morkhasi.setKarbar(this);
        return this;
    }

    public Karbar removeMorkhasi(Morkhasi morkhasi) {
        this.morkhasis.remove(morkhasi);
        morkhasi.setKarbar(null);
        return this;
    }

    public void setMorkhasis(Set<Morkhasi> morkhasis) {
        this.morkhasis = morkhasis;
    }

    public Set<Dore> getDores() {
        return dores;
    }

    public Karbar dores(Set<Dore> dores) {
        this.dores = dores;
        return this;
    }

    public Karbar addDore(Dore dore) {
        this.dores.add(dore);
        dore.setKarbar(this);
        return this;
    }

    public Karbar removeDore(Dore dore) {
        this.dores.remove(dore);
        dore.setKarbar(null);
        return this;
    }

    public void setDores(Set<Dore> dores) {
        this.dores = dores;
    }

    public Set<Negahbani> getNegahbanis() {
        return negahbanis;
    }

    public Karbar negahbanis(Set<Negahbani> negahbanis) {
        this.negahbanis = negahbanis;
        return this;
    }

    public Karbar addNegahbani(Negahbani negahbani) {
        this.negahbanis.add(negahbani);
        negahbani.setKarbar(this);
        return this;
    }

    public Karbar removeNegahbani(Negahbani negahbani) {
        this.negahbanis.remove(negahbani);
        negahbani.setKarbar(null);
        return this;
    }

    public void setNegahbanis(Set<Negahbani> negahbanis) {
        this.negahbanis = negahbanis;
    }

    public Set<BargeMamooriat> getSarparestemamooriats() {
        return sarparestemamooriats;
    }

    public Karbar sarparestemamooriats(Set<BargeMamooriat> bargeMamooriats) {
        this.sarparestemamooriats = bargeMamooriats;
        return this;
    }

    public Karbar addSarparestemamooriat(BargeMamooriat bargeMamooriat) {
        this.sarparestemamooriats.add(bargeMamooriat);
        bargeMamooriat.setSarparast(this);
        return this;
    }

    public Karbar removeSarparestemamooriat(BargeMamooriat bargeMamooriat) {
        this.sarparestemamooriats.remove(bargeMamooriat);
        bargeMamooriat.setSarparast(null);
        return this;
    }

    public void setSarparestemamooriats(Set<BargeMamooriat> bargeMamooriats) {
        this.sarparestemamooriats = bargeMamooriats;
    }

    public Set<Payam> getSandoghVoroodis() {
        return sandoghVoroodis;
    }

    public Karbar sandoghVoroodis(Set<Payam> payams) {
        this.sandoghVoroodis = payams;
        return this;
    }

    public Karbar addSandoghVoroodi(Payam payam) {
        this.sandoghVoroodis.add(payam);
        payam.setKarbarErsalKonande(this);
        return this;
    }

    public Karbar removeSandoghVoroodi(Payam payam) {
        this.sandoghVoroodis.remove(payam);
        payam.setKarbarErsalKonande(null);
        return this;
    }

    public void setSandoghVoroodis(Set<Payam> payams) {
        this.sandoghVoroodis = payams;
    }

    public Set<Payam> getSnadoghKhoroojis() {
        return snadoghKhoroojis;
    }

    public Karbar snadoghKhoroojis(Set<Payam> payams) {
        this.snadoghKhoroojis = payams;
        return this;
    }

    public Karbar addSnadoghKhorooji(Payam payam) {
        this.snadoghKhoroojis.add(payam);
        payam.setKarbarDaryaftKonand(this);
        return this;
    }

    public Karbar removeSnadoghKhorooji(Payam payam) {
        this.snadoghKhoroojis.remove(payam);
        payam.setKarbarDaryaftKonand(null);
        return this;
    }

    public void setSnadoghKhoroojis(Set<Payam> payams) {
        this.snadoghKhoroojis = payams;
    }

    public Set<BargeMamooriat> getBargeMamoorits() {
        return bargeMamoorits;
    }

    public Karbar bargeMamoorits(Set<BargeMamooriat> bargeMamooriats) {
        this.bargeMamoorits = bargeMamooriats;
        return this;
    }

    public Karbar addBargeMamoorit(BargeMamooriat bargeMamooriat) {
        this.bargeMamoorits.add(bargeMamooriat);
        bargeMamooriat.getNafars().add(this);
        return this;
    }

    public Karbar removeBargeMamoorit(BargeMamooriat bargeMamooriat) {
        this.bargeMamoorits.remove(bargeMamooriat);
        bargeMamooriat.getNafars().remove(this);
        return this;
    }

    public void setBargeMamoorits(Set<BargeMamooriat> bargeMamooriats) {
        this.bargeMamoorits = bargeMamooriats;
    }

    public Set<BargeMamooriat> getBinanadeBargeMamoorits() {
        return binanadeBargeMamoorits;
    }

    public Karbar binanadeBargeMamoorits(Set<BargeMamooriat> bargeMamooriats) {
        this.binanadeBargeMamoorits = bargeMamooriats;
        return this;
    }

    public Karbar addBinanadeBargeMamoorit(BargeMamooriat bargeMamooriat) {
        this.binanadeBargeMamoorits.add(bargeMamooriat);
        bargeMamooriat.getBinandes().add(this);
        return this;
    }

    public Karbar removeBinanadeBargeMamoorit(BargeMamooriat bargeMamooriat) {
        this.binanadeBargeMamoorits.remove(bargeMamooriat);
        bargeMamooriat.getBinandes().remove(this);
        return this;
    }

    public void setBinanadeBargeMamoorits(Set<BargeMamooriat> bargeMamooriats) {
        this.binanadeBargeMamoorits = bargeMamooriats;
    }

    public Yegan getYegan() {
        return yegan;
    }

    public Karbar yegan(Yegan yegan) {
        this.yegan = yegan;
        return this;
    }

    public void setYegan(Yegan yegan) {
        this.yegan = yegan;
    }

    public YeganCode getYeganCode() {
        return yeganCode;
    }

    public Karbar yeganCode(YeganCode yeganCode) {
        this.yeganCode = yeganCode;
        return this;
    }

    public void setYeganCode(YeganCode yeganCode) {
        this.yeganCode = yeganCode;
    }

    public Daraje getDaraje() {
        return daraje;
    }

    public Karbar daraje(Daraje daraje) {
        this.daraje = daraje;
        return this;
    }

    public void setDaraje(Daraje daraje) {
        this.daraje = daraje;
    }

    public Semat getSemat() {
        return semat;
    }

    public Karbar semat(Semat semat) {
        this.semat = semat;
        return this;
    }

    public void setSemat(Semat semat) {
        this.semat = semat;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Karbar)) {
            return false;
        }
        return id != null && id.equals(((Karbar) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Karbar{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shoghlSazmani='" + getShoghlSazmani() + "'" +
            ", shoghlAmali='" + getShoghlAmali() + "'" +
            ", codePerseneli='" + getCodePerseneli() + "'" +
            ", bezaneshate='" + isBezaneshate() + "'" +
            ", sazmani='" + isSazmani() + "'" +
            ", tarikhBazneshastegi='" + getTarikhBazneshastegi() + "'" +
            ", tarikhEstekhdam='" + getTarikhEstekhdam() + "'" +
            "}";
    }
}
