import React, { ChangeEvent, useEffect, useMemo, useState } from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import styles from "./TimePicker.module.css";

interface TimePickerProps {
  disabled?: boolean;
  id: string;
  register?: UseFormRegister<any>;
  setValue?: UseFormSetValue<any>;
  minuteResolution: 1 | 5 | 15 | 30 | 60;
  defaultHours?: string;
  defaultMinutes?: string;
  onHourChange?: (val: string) => void;
  onMinuteChange?: (val: string) => void;
}

const TimePicker: React.FC<TimePickerProps> = ({
  disabled,
  id,
  register,
  setValue,
  minuteResolution = 15,
  defaultHours = "09",
  defaultMinutes = "00",
  onHourChange,
  onMinuteChange,
}) => {
  const [hours, setHours] = useState<string>(defaultHours);
  const [minutes, setMinutes] = useState<string>(defaultMinutes);

  useEffect(() => {
    if (defaultHours) {
      setHours(defaultHours);
    }
    if (defaultMinutes) {
      setMinutes(defaultMinutes);
    }
  }, [defaultHours, defaultMinutes]);

  const registerProps = useMemo(
    () => (register ? register(id) : {}),
    [register, id]
  );

  const _onHourChange = (e: ChangeEvent<HTMLSelectElement>) => {
    const hours = e.target.value;
    setHours(hours);
    onHourChange?.(hours);
  };

  const _onMinuteChange = (e: ChangeEvent<HTMLSelectElement>) => {
    const minute = e.target.value;
    setMinutes(minute);
    onMinuteChange?.(minute);
  };

  return (
    <div className={styles.container}>
      <select
        disabled={disabled}
        id={id}
        className={styles.select}
        value={hours}
        onChange={_onHourChange}
        {...registerProps}
      >
        {Array.from(Array(24).keys())
          .map((hour) => `${hour}`.padStart(2, "0"))
          .map((hour) => (
            <option key={`${id}-hour-${hour}`} value={hour}>
              {hour}
            </option>
          ))}
      </select>
      <div>:</div>
      <select
        disabled={disabled}
        id={id}
        className={styles.select}
        value={minutes}
        onChange={_onMinuteChange}
        {...registerProps}
      >
        {Array.from(Array(60).keys())
          .filter((minute) => minute % minuteResolution == 0)
          .map((minute) => `${minute}`.padStart(2, "0"))
          .map((minute) => (
            <option key={`${id}-hour-${minute}`} value={minute}>
              {minute}
            </option>
          ))}
      </select>
    </div>
  );
};

export default TimePicker;
