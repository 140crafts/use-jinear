import React, {useEffect} from 'react';
import {useAppDispatch} from "@/store";
import {api, tagTypes} from "@/api/api.ts";

interface ReloadQueryRefecthHandlerProps {

}

const ReloadQueryRefetchHandler: React.FC<ReloadQueryRefecthHandlerProps> = ({}) => {
    const dispatch = useAppDispatch();
    useEffect(() => {
        if (navigator.onLine) {
            dispatch(api.util.invalidateTags(tagTypes));
        }
    }, []);
    return (null);
}

export default ReloadQueryRefetchHandler;