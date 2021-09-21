package br.com.pucgo.perguntaai.repositories;

import br.com.pucgo.perguntaai.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByName(String nameCourse);
}
