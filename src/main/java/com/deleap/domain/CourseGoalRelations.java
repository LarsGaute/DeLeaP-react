package com.deleap.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CourseGoalRelations.
 */
@Entity
@Table(name = "course_goal_relations")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseGoalRelations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "goal_value")
    private String goalValue;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Goal goal;

    @ManyToOne
    private Goal parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CourseGoalRelations id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoalValue() {
        return this.goalValue;
    }

    public CourseGoalRelations goalValue(String goalValue) {
        this.setGoalValue(goalValue);
        return this;
    }

    public void setGoalValue(String goalValue) {
        this.goalValue = goalValue;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseGoalRelations course(Course course) {
        this.setCourse(course);
        return this;
    }

    public Goal getGoal() {
        return this.goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public CourseGoalRelations goal(Goal goal) {
        this.setGoal(goal);
        return this;
    }

    public Goal getParent() {
        return this.parent;
    }

    public void setParent(Goal goal) {
        this.parent = goal;
    }

    public CourseGoalRelations parent(Goal goal) {
        this.setParent(goal);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseGoalRelations)) {
            return false;
        }
        return id != null && id.equals(((CourseGoalRelations) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseGoalRelations{" +
            "id=" + getId() +
            ", goalValue='" + getGoalValue() + "'" +
            "}";
    }
}
