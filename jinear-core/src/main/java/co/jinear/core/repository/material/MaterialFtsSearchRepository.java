package co.jinear.core.repository.material;

import co.jinear.core.model.entity.material.MaterialFts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaterialFtsSearchRepository extends JpaRepository<MaterialFts, String> {

    @Query(value = """
            select mf.*
            from mv_material_fts mf
                     inner join material m on m.material_id = mf.material_id and m.passive_id is null
                     left join media md on md.media_id = m.media_id
            where mf.fts @@ jinear_fts_prefix_query(:q)
              and mf.workspace_id = :workspaceId
              and (cast(:materialType as integer) is null or m.material_type = cast(:materialType as integer))
              and (m.material_type = 1 or md.upload_status = 1)
              and (
                    :workspaceAdmin = true or
                    m.owner_id = :accountId or
                    m.material_access_type in (1, 3) or
                    (m.material_access_type = 2 and exists (select 1
                                                            from material_access ma
                                                            where ma.material_id = m.material_id
                                                              and ma.account_id = :accountId
                                                              and ma.passive_id is null))
                  )
            order by ts_rank_cd(mf.fts, jinear_fts_prefix_query(:q)) desc,
                     mf.material_id desc
            """,
            countQuery = """
            select count(*)
            from mv_material_fts mf
                     inner join material m on m.material_id = mf.material_id and m.passive_id is null
                     left join media md on md.media_id = m.media_id
            where mf.fts @@ jinear_fts_prefix_query(:q)
              and mf.workspace_id = :workspaceId
              and (cast(:materialType as integer) is null or m.material_type = cast(:materialType as integer))
              and (m.material_type = 1 or md.upload_status = 1)
              and (
                    :workspaceAdmin = true or
                    m.owner_id = :accountId or
                    m.material_access_type in (1, 3) or
                    (m.material_access_type = 2 and exists (select 1
                                                            from material_access ma
                                                            where ma.material_id = m.material_id
                                                              and ma.account_id = :accountId
                                                              and ma.passive_id is null))
                  )
            """,
            nativeQuery = true)
    Page<MaterialFts> search(@Param("q") String query,
                             @Param("workspaceId") String workspaceId,
                             @Param("materialType") Integer materialType,
                             @Param("accountId") String accountId,
                             @Param("workspaceAdmin") boolean workspaceAdmin,
                             Pageable pageable);
}
