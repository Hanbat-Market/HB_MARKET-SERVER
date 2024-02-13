package HM.Hanbat_Market.repository.item;

import HM.Hanbat_Market.domain.entity.ImageFile;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.model.IAttribute;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JpaImageFileRepository implements ImageFileRepository {

    private final EntityManager em;

    @Override
    public Long save(ImageFile imageFile) {
        em.persist(imageFile);
        return imageFile.getId();
    }

    @Override
    public Optional<ImageFile> findById(Long id) {
        return Optional.ofNullable(em.find(ImageFile.class, id));
    }

    @Override
    public List<ImageFile> findAll() {
        return em.createQuery("select i from ImageFile i")
                .getResultList();
    }
}
