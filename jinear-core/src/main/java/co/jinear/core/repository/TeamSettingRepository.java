package co.jinear.core.repository;

import co.jinear.core.model.entity.team.TeamSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamSettingRepository extends JpaRepository<TeamSetting,String> {
}
