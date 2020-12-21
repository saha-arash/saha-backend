package ir.saha.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ir.saha.domain.enumeration.VaziateHesabResi;

/**
 * A HesabResi.
 */
@Entity
@Table(name = "hesab_resi")
public class HesabResi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sal")
    private Integer sal;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaziate_hesab_resi")
    private VaziateHesabResi vaziateHesabResi;

    @OneToOne
    @JoinColumn(unique = true)
    private Gozaresh gozaresh;

    @OneToOne
    @JoinColumn(unique = true)
    private BarnameHesabResi barnameHesabResi;

    @OneToMany(mappedBy = "hesabResi")
    private Set<BargeMamooriat> bargeMamooriats = new HashSet<>();

    @OneToMany(mappedBy = "hesabResi")
    private Set<FileHesabResi> filehas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSal() {
        return sal;
    }

    public HesabResi sal(Integer sal) {
        this.sal = sal;
        return this;
    }

    public void setSal(Integer sal) {
        this.sal = sal;
    }

    public VaziateHesabResi getVaziateHesabResi() {
        return vaziateHesabResi;
    }

    public HesabResi vaziateHesabResi(VaziateHesabResi vaziateHesabResi) {
        this.vaziateHesabResi = vaziateHesabResi;
        return this;
    }

    public void setVaziateHesabResi(VaziateHesabResi vaziateHesabResi) {
        this.vaziateHesabResi = vaziateHesabResi;
    }

    public Gozaresh getGozaresh() {
        return gozaresh;
    }

    public HesabResi gozaresh(Gozaresh gozaresh) {
        this.gozaresh = gozaresh;
        return this;
    }

    public void setGozaresh(Gozaresh gozaresh) {
        this.gozaresh = gozaresh;
    }

    public BarnameHesabResi getBarnameHesabResi() {
        return barnameHesabResi;
    }

    public HesabResi barnameHesabResi(BarnameHesabResi barnameHesabResi) {
        this.barnameHesabResi = barnameHesabResi;
        return this;
    }

    public void setBarnameHesabResi(BarnameHesabResi barnameHesabResi) {
        this.barnameHesabResi = barnameHesabResi;
    }

    public Set<BargeMamooriat> getBargeMamooriats() {
        return bargeMamooriats;
    }

    public HesabResi bargeMamooriats(Set<BargeMamooriat> bargeMamooriats) {
        this.bargeMamooriats = bargeMamooriats;
        return this;
    }

    public HesabResi addBargeMamooriat(BargeMamooriat bargeMamooriat) {
        this.bargeMamooriats.add(bargeMamooriat);
        bargeMamooriat.setHesabResi(this);
        return this;
    }

    public HesabResi removeBargeMamooriat(BargeMamooriat bargeMamooriat) {
        this.bargeMamooriats.remove(bargeMamooriat);
        bargeMamooriat.setHesabResi(null);
        return this;
    }

    public void setBargeMamooriats(Set<BargeMamooriat> bargeMamooriats) {
        this.bargeMamooriats = bargeMamooriats;
    }

    public Set<FileHesabResi> getFilehas() {
        return filehas;
    }

    public HesabResi filehas(Set<FileHesabResi> fileHesabResis) {
        this.filehas = fileHesabResis;
        return this;
    }

    public HesabResi addFileha(FileHesabResi fileHesabResi) {
        this.filehas.add(fileHesabResi);
        fileHesabResi.setHesabResi(this);
        return this;
    }

    public HesabResi removeFileha(FileHesabResi fileHesabResi) {
        this.filehas.remove(fileHesabResi);
        fileHesabResi.setHesabResi(null);
        return this;
    }

    public void setFilehas(Set<FileHesabResi> fileHesabResis) {
        this.filehas = fileHesabResis;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HesabResi)) {
            return false;
        }
        return id != null && id.equals(((HesabResi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "HesabResi{" +
            "id=" + getId() +
            ", sal=" + getSal() +
            ", vaziateHesabResi='" + getVaziateHesabResi() + "'" +
            "}";
    }
}
