package com.deleap.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Goal.
 */
@Entity
@Table(name = "goal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Goal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "goal_focus")
    private String goalFocus;

    @Column(name = "why_achieve_this")
    private String whyAchieveThis;

    @Column(name = "road_ahead")
    private String roadAhead;

    @Column(name = "what_to_achieve")
    private String whatToAchieve;

    @Column(name = "what_to_learn")
    private String whatToLearn;

    @Column(name = "why_focus_on_this")
    private String whyFocusOnThis;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Goal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Goal name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoalFocus() {
        return this.goalFocus;
    }

    public Goal goalFocus(String goalFocus) {
        this.setGoalFocus(goalFocus);
        return this;
    }

    public void setGoalFocus(String goalFocus) {
        this.goalFocus = goalFocus;
    }

    public String getWhyAchieveThis() {
        return this.whyAchieveThis;
    }

    public Goal whyAchieveThis(String whyAchieveThis) {
        this.setWhyAchieveThis(whyAchieveThis);
        return this;
    }

    public void setWhyAchieveThis(String whyAchieveThis) {
        this.whyAchieveThis = whyAchieveThis;
    }

    public String getRoadAhead() {
        return this.roadAhead;
    }

    public Goal roadAhead(String roadAhead) {
        this.setRoadAhead(roadAhead);
        return this;
    }

    public void setRoadAhead(String roadAhead) {
        this.roadAhead = roadAhead;
    }

    public String getWhatToAchieve() {
        return this.whatToAchieve;
    }

    public Goal whatToAchieve(String whatToAchieve) {
        this.setWhatToAchieve(whatToAchieve);
        return this;
    }

    public void setWhatToAchieve(String whatToAchieve) {
        this.whatToAchieve = whatToAchieve;
    }

    public String getWhatToLearn() {
        return this.whatToLearn;
    }

    public Goal whatToLearn(String whatToLearn) {
        this.setWhatToLearn(whatToLearn);
        return this;
    }

    public void setWhatToLearn(String whatToLearn) {
        this.whatToLearn = whatToLearn;
    }

    public String getWhyFocusOnThis() {
        return this.whyFocusOnThis;
    }

    public Goal whyFocusOnThis(String whyFocusOnThis) {
        this.setWhyFocusOnThis(whyFocusOnThis);
        return this;
    }

    public void setWhyFocusOnThis(String whyFocusOnThis) {
        this.whyFocusOnThis = whyFocusOnThis;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Goal)) {
            return false;
        }
        return id != null && id.equals(((Goal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Goal{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", goalFocus='" + getGoalFocus() + "'" +
            ", whyAchieveThis='" + getWhyAchieveThis() + "'" +
            ", roadAhead='" + getRoadAhead() + "'" +
            ", whatToAchieve='" + getWhatToAchieve() + "'" +
            ", whatToLearn='" + getWhatToLearn() + "'" +
            ", whyFocusOnThis='" + getWhyFocusOnThis() + "'" +
            "}";
    }
}
