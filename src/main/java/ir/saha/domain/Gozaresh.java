package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ir.saha.domain.enumeration.VaziatGozaresh;

/**
 * A Gozaresh.
 */
@Entity
@Table(name = "gozaresh")
public class Gozaresh implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaziat")
    private VaziatGozaresh vaziat;

    @OneToMany(mappedBy = "hesabResi")
    private Set<FileGozaresh> filehayegozareshes = new HashSet<>();

    @OneToOne(mappedBy = "gozaresh")
    @JsonIgnore
    private HesabResi hesabResi;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaziatGozaresh getVaziat() {
        return vaziat;
    }

    public Gozaresh vaziat(VaziatGozaresh vaziat) {
        this.vaziat = vaziat;
        return this;
    }

    public void setVaziat(VaziatGozaresh vaziat) {
        this.vaziat = vaziat;
    }

    public Set<FileGozaresh> getFilehayegozareshes() {
        return filehayegozareshes;
    }

    public Gozaresh filehayegozareshes(Set<FileGozaresh> fileGozareshes) {
        this.filehayegozareshes = fileGozareshes;
        return this;
    }

    public Gozaresh addFilehayegozaresh(FileGozaresh fileGozaresh) {
        this.filehayegozareshes.add(fileGozaresh);
        fileGozaresh.setHesabResi(this);
        return this;
    }

    public Gozaresh removeFilehayegozaresh(FileGozaresh fileGozaresh) {
        this.filehayegozareshes.remove(fileGozaresh);
        fileGozaresh.setHesabResi(null);
        return this;
    }

    public void setFilehayegozareshes(Set<FileGozaresh> fileGozareshes) {
        this.filehayegozareshes = fileGozareshes;
    }

    public HesabResi getHesabResi() {
        return hesabResi;
    }

    public Gozaresh hesabResi(HesabResi hesabResi) {
        this.hesabResi = hesabResi;
        return this;
    }

    public void setHesabResi(HesabResi hesabResi) {
        this.hesabResi = hesabResi;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gozaresh)) {
            return false;
        }
        return id != null && id.equals(((Gozaresh) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Gozaresh{" +
            "id=" + getId() +
            ", vaziat='" + getVaziat() + "'" +
            "}";
    }
}
