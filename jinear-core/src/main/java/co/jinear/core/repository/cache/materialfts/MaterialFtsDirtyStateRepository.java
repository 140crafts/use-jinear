package co.jinear.core.repository.cache.materialfts;

public interface MaterialFtsDirtyStateRepository {

    void markDirty();

    boolean isDirty();

    void clearDirty();
}
