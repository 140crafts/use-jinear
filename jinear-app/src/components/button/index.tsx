import cn from "classnames";
import React, {type FC} from "react";
import styles from "./index.module.css";
import {LuLoaderCircle} from "react-icons/lu";
import {Link} from "react-router-dom";

export const ButtonVariants = {
    default: "default",
    filled: "filled",
    filled2: "filled2",
    contrast: "contrast",
    outline: "outline",
    hoverFilled: "hover-filled",
    hoverFilled2: "hover-filled2",
    blur: "blur",
    brandColor: "brandColor",
    link:'link'
};

export const ButtonHeight = {
    default: "height-default",
    mid: "height-mid",
    short: "height-short",
    short2x: "height-short-2x",
};

interface LinkButtonProps {
    disabled?: boolean;
    children?: React.ReactNode;
    href: string;
    target: string;
    className?: string;
    download?: string;
}

const isExternal = (href: string) =>
    /^https?:\/\//.test(href) || href.startsWith("mailto:") || href.startsWith("tel:");

const LinkButton: FC<LinkButtonProps> = ({href, children, target, download, ...props}) => {
    // Use a plain <a> for external links, downloads, or new-tab targets
    if (download || target === "_blank" || isExternal(href)) {
        return (
            <a href={href} target={target} download={download} {...props}>
                {children}
            </a>
        );
    }
    return (
        <Link to={href} {...props}>
            {children}
        </Link>
    );
};


interface BaseButtonProps {
    children?: React.ReactNode;
    className?: string;
}

const BaseButton: FC<BaseButtonProps> = ({children, ...props}) => {
    return (
        <button type="button" {...props}>
            {children}
        </button>
    );
};

interface ButtonProps {
    id?: string;
    children?: React.ReactNode;
    variant?: string;
    heightVariant?: string;
    loading?: boolean;
    disabled?: boolean;
    withShadow?: boolean;
    href?: string;
    target?: string;
    progessClassname?: string;
    progessSize?: number;
    className?: string;
    onClick?: (event?: any) => void;
    onMouseDown?: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
    onMouseOver?: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
    onDoubleClick?: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
    type?: string;
    form?: string;
    style?: any;
    download?: string;
}

const Button: React.FC<ButtonProps> = ({
                                           variant = ButtonVariants.default,
                                           heightVariant = ButtonHeight.default,
                                           loading = false,
                                           disabled = false,
                                           href,
                                           target,
                                           progessClassname,
                                           progessSize = 15,
                                           className,
                                           children,
                                           ...props
                                       }) => {
    const Comp = href ? LinkButton : BaseButton;
    return (
        <Comp
            {...props}
            disabled={disabled}
            href={href as string}
            target={target as string}
            className={cn(styles.button, styles?.[heightVariant], styles?.[variant], className)}
        >
            {loading ? (
                <LuLoaderCircle
                    className={cn([variant == "contrast" ? styles.progessContrast : styles.progess, styles['animateSpin'], progessClassname])}
                    size={progessSize}
                />
            ) : (
                children
            )}
        </Comp>
    );
};

export default Button;
