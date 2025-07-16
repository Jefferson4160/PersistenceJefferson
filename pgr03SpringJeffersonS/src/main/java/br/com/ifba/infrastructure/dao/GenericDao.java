package br.com.ifba.infrastructure.dao;

import br.com.ifba.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext; // Importante: para injetar o EntityManager
import jakarta.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.springframework.stereotype.Repository; // Importe para @Repository
import org.springframework.transaction.annotation.Transactional; // Importe para @Transactional

/**
 * @author Jefferson S
 */
@Repository // Indica ao Spring que esta classe é um componente de acesso a dados (um Bean gerenciado pelo Spring).
public class GenericDao<Entity extends PersistenceEntity> implements GenericIDao<Entity> {

    // Com o Spring Boot e Spring Data JPA, você não gerencia mais a EntityManagerFactory manualmente.
    // O Spring cria e gerencia o EntityManagerFactory (o "túnel").
    // O @PersistenceContext instrui o Spring a injetar o EntityManager (a "via") aqui.
    // Este EntityManager injetado é thread-safe em seu uso transacional gerenciado pelo Spring.
    @PersistenceContext
    protected EntityManager entityManager;

    

    @Override
    @Transactional // Indica que este método deve ser executado dentro de uma transação.
    public Entity save(Entity entity) {
        // O EntityManager 'entityManager' já está injetado e a transação é gerenciada pelo Spring.
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional // Transacional para operações de escrita
    public Entity update(Entity entity) {
        // O Spring gerencia a transação. O 'merge' opera sobre a entidade.
        Entity mergedEntity = entityManager.merge(entity);
        return mergedEntity;
    }

    @Override
    @Transactional
    public void delete(Entity entity) {
        try {
            // --- CORREÇÃO AQUI: Cast explícito para (Entity) ---
            Entity entityToRemove = (Entity) entityManager.find(getTypeClass(), entity.getId());

            if (entityToRemove != null) {
                entityManager.remove(entityToRemove);
                System.out.println("Entidade com ID " + entityToRemove.getId() + " removida com sucesso.");
            } else {
                System.out.println("Entidade com ID " + entity.getId() + " não encontrada para remoção.");
            }
        } catch (Exception e) {
            // ... (tratamento de erro) ...
            throw new RuntimeException("Falha ao excluir", e);
        }
    }

    // Métodos de leitura geralmente não precisam de @Transactional (a menos que você queira read-only transactions).
    @Override
    public List<Entity> findAll() {
        // Usa o 'entityManager' injetado.
        List<Entity> result = (List<Entity>) entityManager.createQuery("from " + getTypeClass().getName() + " c ORDER BY c.id ASC", getTypeClass())
                                          .getResultList();
        return result;
    }

    @Override
    public Entity findById(Long id) {
        // Usa o 'entityManager' injetado.
        Entity foundEntity = (Entity) entityManager.find(getTypeClass(), id);

        if (foundEntity == null) {
            System.out.println("Entidade com ID " + id + " não encontrada (findById).");
        }
        return foundEntity;
    }

    // O getTypeClass() permanece inalterado.
    protected Class<?> getTypeClass() {
        Class<?> clazz = (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return clazz;
    }
}