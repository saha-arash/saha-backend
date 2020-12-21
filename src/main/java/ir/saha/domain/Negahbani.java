package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Negahbani.
 */
@Entity
@Table(name = "negahbani")
public class Negahbani implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "begin")
    private Instant begin;

    @Column(name = "end")
    private Instant end;

    @ManyToOne
    @JsonIgnoreProperties("negahbanis")
    private Karbar karbar;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBegin() {
        return begin;
    }

    public Negahbani begin(Instant begin) {
        this.begin = begin;
        return this;
    }

    public void setBegin(Instant begin) {
        this.begin = begin;
    }

    public Instant getEnd() {
        return end;
    }

    public Negahbani end(Instant end) {
        this.end = end;
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Karbar getKarbar() {
        return karbar;
    }

    public Negahbani karbar(Karbar karbar) {
        this.karbar = karbar;
        return this;
    }

    public void setKarbar(Karbar karbar) {
        this.karbar = karbar;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Negahbani)) {
            return false;
        }
        return id != null && id.equals(((Negahbani) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Negahbani{" +
            "id=" + getId() +
            ", begin='" + getBegin() + "'" +
            ", end='" + getEnd() + "'" +
            "}";
    }
}
