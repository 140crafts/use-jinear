package co.jinear.core.converter.calendar;

import co.jinear.core.converter.team.TeamMembershipTeamVisibilityTypeMapConverter;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.request.calendar.CalendarEventFilterRequest;
import co.jinear.core.model.vo.calendar.CalendarEventSearchFilterVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CalendarEventFilterRequestToTaskSearchFilterVoConverter {

    @Autowired
    protected TeamMembershipTeamVisibilityTypeMapConverter teamMembershipTeamVisibilityTypeMapConverter;

    @Mapping(target = "teamMemberMap", expression = "java(teamMembershipTeamVisibilityTypeMapConverter.convert(memberships))")
    public abstract CalendarEventSearchFilterVo convert(CalendarEventFilterRequest calendarEventFilterRequest, List<TeamMemberDto> memberships);


    @Mapping(target = "teamMemberMap", expression = "java(teamMembershipTeamVisibilityTypeMapConverter.convert(memberships))")
    public abstract CalendarEventSearchFilterVo convert(String workspaceId, ZonedDateTime timespanStart, ZonedDateTime timespanEnd, List<TeamMemberDto> memberships);
}
