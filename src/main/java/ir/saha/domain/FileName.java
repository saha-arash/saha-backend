package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A FileName.
 */
@Entity
@Table(name = "file_name")
public class FileName implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "madrak")
    private byte[] madrak;

    @Column(name = "madrak_content_type")
    private String madrakContentType;

    @ManyToOne
    @JsonIgnoreProperties("madareks")
    private Payam name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getMadrak() {
        return madrak;
    }

    public FileName madrak(byte[] madrak) {
        this.madrak = madrak;
        return this;
    }

    public void setMadrak(byte[] madrak) {
        this.madrak = madrak;
    }

    public String getMadrakContentType() {
        return madrakContentType;
    }

    public FileName madrakContentType(String madrakContentType) {
        this.madrakContentType = madrakContentType;
        return this;
    }

    public void setMadrakContentType(String madrakContentType) {
        this.madrakContentType = madrakContentType;
    }

    public Payam getName() {
        return name;
    }

    public FileName name(Payam payam) {
        this.name = payam;
        return this;
    }

    public void setName(Payam payam) {
        this.name = payam;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileName)) {
            return false;
        }
        return id != null && id.equals(((FileName) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FileName{" +
            "id=" + getId() +
            ", madrak='" + getMadrak() + "'" +
            ", madrakContentType='" + getMadrakContentType() + "'" +
            "}";
    }
}
