import { initializePaddle, getPaddleInstance } from "@paddle/paddle-js";
import { __DEV__, PADDLE_VENDOR_ID } from "@/util/constants";

export function initPaddle() {
    return initializePaddle({
        version: "classic",
        environment: __DEV__ ? "sandbox" : "production",
        vendor: PADDLE_VENDOR_ID,
    } as any);
}

export function getPaddle() {
    return getPaddleInstance("classic");
}
