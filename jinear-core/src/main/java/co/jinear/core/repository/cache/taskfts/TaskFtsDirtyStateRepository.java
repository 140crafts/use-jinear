package co.jinear.core.repository.cache.taskfts;

/**
 * Tracks, in shared storage (Redis), whether {@code mv_task_fts} has pending changes that
 * still need to be reflected by a materialized view refresh. Shared so that a write handled
 * on one node is picked up by whichever node runs the scheduled refresh.
 */
public interface TaskFtsDirtyStateRepository {

    void markDirty();

    boolean isDirty();

    void clearDirty();
}
