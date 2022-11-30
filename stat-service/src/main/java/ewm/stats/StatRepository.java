package ewm.stats;

import ewm.stats.dto.StatDto;
import ewm.stats.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long>, JpaSpecificationExecutor<Stat> {
    /*
    @Query("SELECT new ewm.stats.dto.StatDto(st.app, st.uri, COUNT(st.ip)) " +
            "FROM Stat AS st WHERE st.timestamp > :start and st.timestamp < :end and st.uri IN :uris " +
            "GROUP BY st.app, st.uri")
    List<StatDto> countIp(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

    @Query("SELECT new  ewm.stats.dto.StatDto(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM Stat AS st WHERE st.timestamp > :start and st.timestamp < :end and st.uri IN :uris " +
            "GROUP BY st.app, st.uri")
    List<StatDto> countIpDistinct(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

     */
    @Query(value = "SELECT new ewm.stats.dto.StatDto(e.app,e.uri,COUNT(e.ip)) " +
            "FROM Stat e WHERE (e.timestamp BETWEEN ?1 AND ?2) AND (e.uri IN ?3 or ?3 IS NULL) " +
            "GROUP BY e.app,e.uri")

    List<StatDto> countIp(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

    @Query("SELECT new ewm.stats.dto.StatDto(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM Stat AS st WHERE st.timestamp > :start and st.timestamp < :end and st.uri IN :uris " +
            "GROUP BY st.app, st.uri")
    List<StatDto> countIpDistinct(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);
}
