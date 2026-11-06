package co.jinear.core.repository.cache.notefts;

public interface NoteFtsDirtyStateRepository {

    void markDirty();

    boolean isDirty();

    void clearDirty();
}
