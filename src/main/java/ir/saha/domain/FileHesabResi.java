package ir.saha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

import ir.saha.domain.enumeration.FileType;

/**
 * A FileHesabResi.
 */
@Entity
@Table(name = "file_hesab_resi")
public class FileHesabResi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    @Column(name = "shomare")
    private Integer shomare;

    @Column(name = "tarikh_name")
    private Instant tarikhName;

    @Column(name = "mozoo")
    private String mozoo;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileType fileType;

    @ManyToOne
    @JsonIgnoreProperties("filehas")
    private HesabResi hesabResi;

    @ManyToOne
    @JsonIgnoreProperties("failhas")
    private BarnameHesabResi barnameHesabResi;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public FileHesabResi file(byte[] file) {
        this.file = file;
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public FileHesabResi fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Integer getShomare() {
        return shomare;
    }

    public FileHesabResi shomare(Integer shomare) {
        this.shomare = shomare;
        return this;
    }

    public void setShomare(Integer shomare) {
        this.shomare = shomare;
    }

    public Instant getTarikhName() {
        return tarikhName;
    }

    public FileHesabResi tarikhName(Instant tarikhName) {
        this.tarikhName = tarikhName;
        return this;
    }

    public void setTarikhName(Instant tarikhName) {
        this.tarikhName = tarikhName;
    }

    public String getMozoo() {
        return mozoo;
    }

    public FileHesabResi mozoo(String mozoo) {
        this.mozoo = mozoo;
        return this;
    }

    public void setMozoo(String mozoo) {
        this.mozoo = mozoo;
    }

    public FileType getFileType() {
        return fileType;
    }

    public FileHesabResi fileType(FileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public HesabResi getHesabResi() {
        return hesabResi;
    }

    public FileHesabResi hesabResi(HesabResi hesabResi) {
        this.hesabResi = hesabResi;
        return this;
    }

    public void setHesabResi(HesabResi hesabResi) {
        this.hesabResi = hesabResi;
    }

    public BarnameHesabResi getBarnameHesabResi() {
        return barnameHesabResi;
    }

    public FileHesabResi barnameHesabResi(BarnameHesabResi barnameHesabResi) {
        this.barnameHesabResi = barnameHesabResi;
        return this;
    }

    public void setBarnameHesabResi(BarnameHesabResi barnameHesabResi) {
        this.barnameHesabResi = barnameHesabResi;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileHesabResi)) {
            return false;
        }
        return id != null && id.equals(((FileHesabResi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FileHesabResi{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            ", shomare=" + getShomare() +
            ", tarikhName='" + getTarikhName() + "'" +
            ", mozoo='" + getMozoo() + "'" +
            ", fileType='" + getFileType() + "'" +
            "}";
    }
}
