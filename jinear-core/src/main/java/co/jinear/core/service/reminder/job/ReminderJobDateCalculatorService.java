package co.jinear.core.service.reminder.job;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.enumtype.reminder.RepeatType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderJobDateCalculatorService {

    private final UnaryOperator<ZonedDateTime> calculateForNextHourlyTime = zonedDateTime -> zonedDateTime.plusHours(1L);
    private final UnaryOperator<ZonedDateTime> calculateForNextDailyTime = zonedDateTime -> zonedDateTime.plusDays(1L);
    private final UnaryOperator<ZonedDateTime> calculateForNextWeeklyTime = zonedDateTime -> zonedDateTime.plusWeeks(1L);
    private final UnaryOperator<ZonedDateTime> calculateForNextBiWeeklyTime = zonedDateTime -> zonedDateTime.plusWeeks(2L);
    private final UnaryOperator<ZonedDateTime> calculateForNextMonthlyTime = zonedDateTime -> zonedDateTime.plusMonths(1L);
    private final UnaryOperator<ZonedDateTime> calculateForNextEvery3MonthTime = zonedDateTime -> zonedDateTime.plusMonths(3L);
    private final UnaryOperator<ZonedDateTime> calculateForNextEvery6MonthTime = zonedDateTime -> zonedDateTime.plusMonths(6L);
    private final UnaryOperator<ZonedDateTime> calculateForNextYearlyTime = zonedDateTime -> zonedDateTime.plusYears(1L);

    // WARN: Do not add a type with a repeat time less than ReminderUtilities.REMINDER_INTERVAL
    private final Map<RepeatType, UnaryOperator<ZonedDateTime>> nextDateCalculateStrategyMap = Map.of(
            RepeatType.HOURLY, calculateForNextHourlyTime,
            RepeatType.DAILY, calculateForNextDailyTime,
            RepeatType.WEEKLY, calculateForNextWeeklyTime,
            RepeatType.BIWEEKLY, calculateForNextBiWeeklyTime,
            RepeatType.MONTHLY, calculateForNextMonthlyTime,
            RepeatType.EVERY_3_MONTHS, calculateForNextEvery3MonthTime,
            RepeatType.EVERY_6_MONTHS, calculateForNextEvery6MonthTime,
            RepeatType.YEARLY, calculateForNextYearlyTime
    );

    public Optional<ZonedDateTime> calculateNextDate(ReminderJobDto reminderJobDto) {
        log.info("Calculate next date for reminder has started. reminderJobDto: {}", reminderJobDto);
        RepeatType repeatType = Optional.of(reminderJobDto).map(ReminderJobDto::getReminder).map(ReminderDto::getRepeatType).orElseThrow(BusinessException::new);
        UnaryOperator<ZonedDateTime> function = nextDateCalculateStrategyMap.get(repeatType);
        return Optional.ofNullable(function)
                .map(f -> retrieveFirstFutureDate(reminderJobDto, f));
    }

    @NonNull
    private ZonedDateTime retrieveFirstFutureDate(ReminderJobDto reminderJobDto, UnaryOperator<ZonedDateTime> function) {
        log.info("Retrieve first future date has started.");
        ZonedDateTime jobDate = reminderJobDto.getDate();
        ZonedDateTime nextDate = function.apply(jobDate);
        while (nextDate.isBefore(ZonedDateTime.now())) {
            log.info("Calculated next date ({}) is past. calculating next.", nextDate);
            nextDate = function.apply(nextDate);
        }
        log.info("Next date calculation has completed. nextDate: {}", nextDate);
        return nextDate;
    }
}
