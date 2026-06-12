export type DeviceTier = "high" | "low";

export interface DeviceProfile {
    tier: DeviceTier;
    saveData: boolean;
    slowNetwork: boolean;
}

interface NavigatorWithCapabilities extends Navigator {
    deviceMemory?: number;
    connection?: {
        saveData?: boolean;
        effectiveType?: string;
    };
}

// All signals are feature-detected; browsers that expose none of them (e.g. Safari
// lacks deviceMemory and connection) resolve to the "high" tier so they are not
// penalized by missing data.
export const getDeviceProfile = (): DeviceProfile => {
    const nav = typeof navigator === "undefined" ? undefined : (navigator as NavigatorWithCapabilities);
    const lowMemory = nav?.deviceMemory !== undefined && nav.deviceMemory <= 4;
    const lowCores = nav?.hardwareConcurrency !== undefined && nav.hardwareConcurrency <= 4;
    const effectiveType = nav?.connection?.effectiveType;
    return {
        tier: lowMemory || lowCores ? "low" : "high",
        saveData: nav?.connection?.saveData === true,
        slowNetwork: effectiveType === "2g" || effectiveType === "slow-2g"
    };
};
