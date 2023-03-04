import cn from "classnames";
import React, { useState } from "react";
import styles from "./SegmentedControl.module.css";
import SelectionBox from "./selectionBox/SelectionBox";

export interface Segment {
  segmentId?: string;
  label: string;
  longLabel?: string;
  icon?: any;
  value: string;
  inputProps?: any;
  contentContainerClassName?: string;
}

interface SegmentedControlProps {
  id?: string;
  segments: Segment[];
  name: string;
  defaultIndex: number;
  callback?: (value: string, index: number) => any;
  segmentLabelClassName?: string;
  contentContainerClassName?: string;
}

const SegmentedControl: React.FC<SegmentedControlProps> = ({
  id,
  name,
  callback,
  defaultIndex = 0,
  segmentLabelClassName,
  segments,
  contentContainerClassName,
}) => {
  const [activeIndex, setActiveIndex] = useState<number>(defaultIndex);
  const onInputChange = (value: string, index: number) => {
    setActiveIndex(index);
    callback?.(value, index);
  };

  return !segments ? null : (
    <div id={id} className={cn(styles.container, contentContainerClassName)}>
      <div className={styles.wrapper}>
        {segments?.map((item, i) => (
          <div className={styles.inputContainer} key={`segment-${name}-${i}`}>
            {i === activeIndex && <SelectionBox id={`segment-${name}`} />}
            <input
              type="radio"
              value={item.value}
              id={item.segmentId ? item.segmentId : item.label}
              name={item.segmentId ? item.segmentId : name + "-" + i}
              checked={i === activeIndex}
              {...item?.inputProps}
              onChange={(e) => {
                onInputChange(item.value, i);
                item?.inputProps?.onChange?.(e);
              }}
            />
            <label htmlFor={item.segmentId ? item.segmentId : item.label} className={cn(styles.label, segmentLabelClassName)}>
              {item?.icon}
              {item.label}
            </label>
          </div>
        ))}
      </div>
      {segments?.[activeIndex]?.longLabel && (
        <div className={styles.longLabelContainer}>
          <textarea className={styles.longLabel} value={segments?.[activeIndex]?.longLabel} disabled />
        </div>
      )}
    </div>
  );
};

export default SegmentedControl;
