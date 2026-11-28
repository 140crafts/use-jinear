package co.jinear.core.config.liquibase;

import de.huxhorn.sulky.ulid.ULID;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

// changelog-v28.xml references this class by name; renaming, moving or deleting it breaks the migration.
@Slf4j
public class GrainedMaterialOwnerAccessBackfillChange implements CustomTaskChange {

    private static final String SELECT_GRAINED_MATERIALS_WITHOUT_OWNER_ACCESS = """
            select m.material_id, m.owner_id
            from material m
            where m.material_access_type = 2
              and m.passive_id is null
              and not exists (select 1
                              from material_access ma
                              where ma.material_id = m.material_id
                                and ma.account_id = m.owner_id
                                and ma.passive_id is null)
            """;

    private static final String INSERT_MATERIAL_ACCESS = """
            insert into material_access (material_access_id, material_id, account_id)
            values (?, ?, ?)
            """;

    private final ULID ulid = new ULID();
    private int insertedCount = 0;

    @Override
    public void execute(Database database) throws CustomChangeException {
        JdbcConnection jdbcConnection = (JdbcConnection) database.getConnection();
        Connection connection = jdbcConnection.getUnderlyingConnection();
        try (PreparedStatement select = connection.prepareStatement(SELECT_GRAINED_MATERIALS_WITHOUT_OWNER_ACCESS);
             PreparedStatement insert = connection.prepareStatement(INSERT_MATERIAL_ACCESS);
             ResultSet materials = select.executeQuery()) {
            addOwnerAccessBatch(materials, insert);
            insert.executeBatch();
        } catch (SQLException e) {
            throw new CustomChangeException(e);
        }
        log.info("Grained material owner access backfill has completed. insertedCount: {}", insertedCount);
    }

    @Override
    public String getConfirmationMessage() {
        return "Owner access added to %d grained materials".formatted(insertedCount);
    }

    @Override
    public void setUp() {
    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
    }

    @Override
    public ValidationErrors validate(Database database) {
        return new ValidationErrors();
    }

    private void addOwnerAccessBatch(ResultSet materials, PreparedStatement insert) throws SQLException {
        while (materials.next()) {
            String materialAccessId = ulid.nextULID().toLowerCase(Locale.ROOT);
            insert.setString(1, materialAccessId);
            insert.setString(2, materials.getString("material_id"));
            insert.setString(3, materials.getString("owner_id"));
            insert.addBatch();
            insertedCount++;
        }
    }
}
