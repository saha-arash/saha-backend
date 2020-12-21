package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Payam.
 */
@Entity
@Table(name = "payam")
public class Payam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "onvan")
    private String onvan;

    @Lob
    @Column(name = "matn")
    private String matn;

    @OneToMany(mappedBy = "name")
    private Set<FileName> madareks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("sandoghVoroodis")
    private Karbar karbarErsalKonande;

    @ManyToOne
    @JsonIgnoreProperties("snadoghKhoroojis")
    private Karbar karbarDaryaftKonand;

    @ManyToOne
    @JsonIgnoreProperties("sandoghVoroodis")
    private Yegan yeganErsalKonanade;

    @ManyToOne
    @JsonIgnoreProperties("snadoghKhoroojis")
    private Yegan yeganDaryaftKonanade;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOnvan() {
        return onvan;
    }

    public Payam onvan(String onvan) {
        this.onvan = onvan;
        return this;
    }

    public void setOnvan(String onvan) {
        this.onvan = onvan;
    }

    public String getMatn() {
        return matn;
    }

    public Payam matn(String matn) {
        this.matn = matn;
        return this;
    }

    public void setMatn(String matn) {
        this.matn = matn;
    }

    public Set<FileName> getMadareks() {
        return madareks;
    }

    public Payam madareks(Set<FileName> fileNames) {
        this.madareks = fileNames;
        return this;
    }

    public Payam addMadarek(FileName fileName) {
        this.madareks.add(fileName);
        fileName.setName(this);
        return this;
    }

    public Payam removeMadarek(FileName fileName) {
        this.madareks.remove(fileName);
        fileName.setName(null);
        return this;
    }

    public void setMadareks(Set<FileName> fileNames) {
        this.madareks = fileNames;
    }

    public Karbar getKarbarErsalKonande() {
        return karbarErsalKonande;
    }

    public Payam karbarErsalKonande(Karbar karbar) {
        this.karbarErsalKonande = karbar;
        return this;
    }

    public void setKarbarErsalKonande(Karbar karbar) {
        this.karbarErsalKonande = karbar;
    }

    public Karbar getKarbarDaryaftKonand() {
        return karbarDaryaftKonand;
    }

    public Payam karbarDaryaftKonand(Karbar karbar) {
        this.karbarDaryaftKonand = karbar;
        return this;
    }

    public void setKarbarDaryaftKonand(Karbar karbar) {
        this.karbarDaryaftKonand = karbar;
    }

    public Yegan getYeganErsalKonanade() {
        return yeganErsalKonanade;
    }

    public Payam yeganErsalKonanade(Yegan yegan) {
        this.yeganErsalKonanade = yegan;
        return this;
    }

    public void setYeganErsalKonanade(Yegan yegan) {
        this.yeganErsalKonanade = yegan;
    }

    public Yegan getYeganDaryaftKonanade() {
        return yeganDaryaftKonanade;
    }

    public Payam yeganDaryaftKonanade(Yegan yegan) {
        this.yeganDaryaftKonanade = yegan;
        return this;
    }

    public void setYeganDaryaftKonanade(Yegan yegan) {
        this.yeganDaryaftKonanade = yegan;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payam)) {
            return false;
        }
        return id != null && id.equals(((Payam) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Payam{" +
            "id=" + getId() +
            ", onvan='" + getOnvan() + "'" +
            ", matn='" + getMatn() + "'" +
            "}";
    }
}
