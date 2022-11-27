import cn from 'classnames';
import React, { ChangeEvent } from 'react';
import styles from './TextInput.module.css';

interface TextAreaInputProps {
    value?: string | number | readonly string[] | undefined;
    placeholder?: string;
    className?: string;
    name?: string;
    type?: 'text' | 'email' | 'number' | 'password' | 'tel' | 'url' | 'date' | 'checkbox' | 'hidden';
}

export interface TextInputProps {
    type?: 'text' | 'email' | 'number' | 'password' | 'tel' | 'url' | 'date' | 'checkbox' | 'hidden';
    placeholder?: string;
    value?: string;
    checked?: boolean;
    className?: string;
    onEnterCallback?: () => void;
    name?: string;
    onChange?: (e: ChangeEvent<HTMLInputElement>) => void;
    min?: number | string;
    max?: number | string;
    step?: number | string | undefined;
    disabled?: boolean;
}

export const TextInput = React.forwardRef<HTMLInputElement, TextInputProps>(
    ({ type, value, placeholder, className, onEnterCallback, min, step, disabled, ...props }, ref) => {
        const isEnter = (event: React.KeyboardEvent<HTMLDivElement>) => {
            if (event.key === 'Enter') {
                onEnterCallback?.();
            }
        };
        return (
            <input
                {...props}
                ref={ref}
                type={type}
                value={value}
                placeholder={placeholder}
                onKeyDown={isEnter}
                className={cn([styles.input, disabled && styles.disabled, className])}
                min={min}
                step={step}
                disabled={disabled}></input>
        );
    },
);
TextInput.displayName = 'TextInput';

export const TextAreaInput = React.forwardRef<HTMLTextAreaElement, TextAreaInputProps>(({ value, name, className, ...props }, ref) => {
    return <textarea ref={ref} value={value} name={name} className={cn([styles.input, className])} {...props}></textarea>;
});
TextAreaInput.displayName = 'TextAreaInput';
