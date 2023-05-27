package com.deleap.domain;

import com.deleap.domain.enumeration.AcademyType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Academy er lærestedet. Her må det være mulig å ha \"Parent\" Academy slik at man kan bygge opp hirarki av læresteder.\neks. personen X oppretter sitt eige academy y, og slår seg sammen under academy z osv.
 */
@Schema(
    description = "Academy er lærestedet. Her må det være mulig å ha \"Parent\" Academy slik at man kan bygge opp hirarki av læresteder.\neks. personen X oppretter sitt eige academy y, og slår seg sammen under academy z osv."
)
@Entity
@Table(name = "academy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Academy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(min = 2)
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AcademyType type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Academy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Academy name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AcademyType getType() {
        return this.type;
    }

    public Academy type(AcademyType type) {
        this.setType(type);
        return this;
    }

    public void setType(AcademyType type) {
        this.type = type;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Academy)) {
            return false;
        }
        return id != null && id.equals(((Academy) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Academy{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
