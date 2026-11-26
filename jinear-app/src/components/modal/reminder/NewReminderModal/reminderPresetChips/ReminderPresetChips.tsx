import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locales/useTranslation";
import {isEqual} from "date-fns";
import React, {useMemo} from "react";
import {type ReminderPreset, reminderPresets} from "../reminderForm";

interface ReminderPresetChipsProps {
    value: Date | null;
    onSelect: (date: Date) => void;
}

const ReminderPresetChips: React.FC<ReminderPresetChipsProps> = ({value, onSelect}) => {
    const {t} = useTranslation();
    const presets = useMemo(() => reminderPresets(new Date()), []);

    const isSelected = (preset: ReminderPreset) => value != null && isEqual(value, preset.date);

    return (
        <>
            {presets.map(preset => (
                <Button
                    key={preset.labelKey}
                    heightVariant={ButtonHeight.short}
                    variant={isSelected(preset) ? ButtonVariants.contrast : ButtonVariants.filled}
                    onClick={() => onSelect(preset.date)}
                >
                    {t(preset.labelKey)}
                </Button>
            ))}
        </>
    );
};

export default ReminderPresetChips;
