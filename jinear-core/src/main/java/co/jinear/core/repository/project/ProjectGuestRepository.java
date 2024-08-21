package co.jinear.core.repository.project;

import co.jinear.core.model.entity.project.ProjectGuest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectGuestRepository extends JpaRepository<ProjectGuest, String> {
}
