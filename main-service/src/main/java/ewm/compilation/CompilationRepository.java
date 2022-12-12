package ewm.compilation;

import ewm.compilation.model.Compilation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompilationRepository extends JpaRepository<Compilation, Long>,
        JpaSpecificationExecutor<Compilation> {

    @Override
    @EntityGraph(attributePaths = {"events"})
    Page<Compilation> findAll(Specification<Compilation> spec, Pageable pageable);
}
