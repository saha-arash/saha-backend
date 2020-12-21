package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ir.saha.domain.enumeration.NoeBarnameHesabResi;

/**
 * A BarnameHesabResi.
 */
@Entity
@Table(name = "barname_hesab_resi")
public class BarnameHesabResi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "noe_barname_hesab_resi")
    private NoeBarnameHesabResi noeBarnameHesabResi;

    @OneToMany(mappedBy = "barnameHesabResi")
    private Set<FileHesabResi> failhas = new HashSet<>();

    @OneToOne(mappedBy = "barnameHesabResi")
    @JsonIgnore
    private HesabResi hesabResi;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NoeBarnameHesabResi getNoeBarnameHesabResi() {
        return noeBarnameHesabResi;
    }

    public BarnameHesabResi noeBarnameHesabResi(NoeBarnameHesabResi noeBarnameHesabResi) {
        this.noeBarnameHesabResi = noeBarnameHesabResi;
        return this;
    }

    public void setNoeBarnameHesabResi(NoeBarnameHesabResi noeBarnameHesabResi) {
        this.noeBarnameHesabResi = noeBarnameHesabResi;
    }

    public Set<FileHesabResi> getFailhas() {
        return failhas;
    }

    public BarnameHesabResi failhas(Set<FileHesabResi> fileHesabResis) {
        this.failhas = fileHesabResis;
        return this;
    }

    public BarnameHesabResi addFailha(FileHesabResi fileHesabResi) {
        this.failhas.add(fileHesabResi);
        fileHesabResi.setBarnameHesabResi(this);
        return this;
    }

    public BarnameHesabResi removeFailha(FileHesabResi fileHesabResi) {
        this.failhas.remove(fileHesabResi);
        fileHesabResi.setBarnameHesabResi(null);
        return this;
    }

    public void setFailhas(Set<FileHesabResi> fileHesabResis) {
        this.failhas = fileHesabResis;
    }

    public HesabResi getHesabResi() {
        return hesabResi;
    }

    public BarnameHesabResi hesabResi(HesabResi hesabResi) {
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
        if (!(o instanceof BarnameHesabResi)) {
            return false;
        }
        return id != null && id.equals(((BarnameHesabResi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BarnameHesabResi{" +
            "id=" + getId() +
            ", noeBarnameHesabResi='" + getNoeBarnameHesabResi() + "'" +
            "}";
    }
}
