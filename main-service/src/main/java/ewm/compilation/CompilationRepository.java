package ewm.compilation;

import ewm.compilation.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompilationRepository extends JpaRepository<Compilation, Long>,
        JpaSpecificationExecutor<Compilation> {
}
