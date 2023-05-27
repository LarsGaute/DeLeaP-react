package com.deleap.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CourseGoalTree.
 */
@Entity
@Table(name = "course_goal_tree")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseGoalTree implements Serializable {

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
    @JsonIgnoreProperties(value = { "course", "goal", "parent" }, allowSetters = true)
    private CourseGoalRelations coursegoal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CourseGoalTree id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoalValue() {
        return this.goalValue;
    }

    public CourseGoalTree goalValue(String goalValue) {
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

    public CourseGoalTree course(Course course) {
        this.setCourse(course);
        return this;
    }

    public CourseGoalRelations getCoursegoal() {
        return this.coursegoal;
    }

    public void setCoursegoal(CourseGoalRelations courseGoalRelations) {
        this.coursegoal = courseGoalRelations;
    }

    public CourseGoalTree coursegoal(CourseGoalRelations courseGoalRelations) {
        this.setCoursegoal(courseGoalRelations);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseGoalTree)) {
            return false;
        }
        return id != null && id.equals(((CourseGoalTree) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseGoalTree{" +
            "id=" + getId() +
            ", goalValue='" + getGoalValue() + "'" +
            "}";
    }
}
