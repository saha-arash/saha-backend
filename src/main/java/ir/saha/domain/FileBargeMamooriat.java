package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A FileBargeMamooriat.
 */
@Entity
@Table(name = "file_barge_mamooriat")
public class FileBargeMamooriat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "madarek")
    private byte[] madarek;

    @Column(name = "madarek_content_type")
    private String madarekContentType;

    @ManyToOne
    @JsonIgnoreProperties("madareks")
    private BargeMamooriat bargeMamooriat;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getMadarek() {
        return madarek;
    }

    public FileBargeMamooriat madarek(byte[] madarek) {
        this.madarek = madarek;
        return this;
    }

    public void setMadarek(byte[] madarek) {
        this.madarek = madarek;
    }

    public String getMadarekContentType() {
        return madarekContentType;
    }

    public FileBargeMamooriat madarekContentType(String madarekContentType) {
        this.madarekContentType = madarekContentType;
        return this;
    }

    public void setMadarekContentType(String madarekContentType) {
        this.madarekContentType = madarekContentType;
    }

    public BargeMamooriat getBargeMamooriat() {
        return bargeMamooriat;
    }

    public FileBargeMamooriat bargeMamooriat(BargeMamooriat bargeMamooriat) {
        this.bargeMamooriat = bargeMamooriat;
        return this;
    }

    public void setBargeMamooriat(BargeMamooriat bargeMamooriat) {
        this.bargeMamooriat = bargeMamooriat;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileBargeMamooriat)) {
            return false;
        }
        return id != null && id.equals(((FileBargeMamooriat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FileBargeMamooriat{" +
            "id=" + getId() +
            ", madarek='" + getMadarek() + "'" +
            ", madarekContentType='" + getMadarekContentType() + "'" +
            "}";
    }
}
