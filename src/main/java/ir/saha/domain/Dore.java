package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Dore.
 */
@Entity
@Table(name = "dore")
public class Dore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "begin")
    private Instant begin;

    @Column(name = "end")
    private Instant end;

    @ManyToOne
    @JsonIgnoreProperties("dores")
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

    public Dore begin(Instant begin) {
        this.begin = begin;
        return this;
    }

    public void setBegin(Instant begin) {
        this.begin = begin;
    }

    public Instant getEnd() {
        return end;
    }

    public Dore end(Instant end) {
        this.end = end;
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Karbar getKarbar() {
        return karbar;
    }

    public Dore karbar(Karbar karbar) {
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
        if (!(o instanceof Dore)) {
            return false;
        }
        return id != null && id.equals(((Dore) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dore{" +
            "id=" + getId() +
            ", begin='" + getBegin() + "'" +
            ", end='" + getEnd() + "'" +
            "}";
    }
}
