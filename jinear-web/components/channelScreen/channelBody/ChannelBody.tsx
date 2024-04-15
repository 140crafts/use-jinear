import React from 'react';
import styles from './ChannelBody.module.css';

interface ChannelBodyProps {
  workspaceName: string;
  channelId: string;
  workspaceId: string;
}

const ChannelBody: React.FC<ChannelBodyProps> =({workspaceName, channelId, workspaceId })=>{

    return (
        <div className={styles.container}>
            body
        </div>
    );
}

export default ChannelBody;