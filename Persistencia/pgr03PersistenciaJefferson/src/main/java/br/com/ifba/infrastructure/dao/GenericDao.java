/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.infrastructure.dao;

import br.com.ifba.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 *
 * @author Jefferson S
 */
public class GenericDao<Entity extends PersistenceEntity> implements GenericIDao<Entity> {
    
    //Esse é o meu túnel
    protected static EntityManagerFactory entityManagerFactory;
    //Um bloco estatico de inicialização é executado uma unica vez quando a classe genericDao é carregada pela JVM
    static{
        try{
            //Cria a EntityManagerFactory que é um objeto criado somente uma vez
            //Ele q irá ler o persistence.xml
            entityManagerFactory = Persistence.createEntityManagerFactory("gerenciamento_curso");
            
        } catch (Exception e){
            //Aqui na mensagem de erro o getMessage retorna uma mensagem curta descrevendo o prq do erro
            System.err.println("Erro fatal ao inicializar JPA no GenericDao: "+e.getMessage());
            e.printStackTrace();
            //O sistema é encerrado pois sem o EntityManagerFactory o programa não consegue interagir com o BD
            System.exit(1);
        } 
        
    }

    @Override
    public Entity save(Entity entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            //Inicia uma transação com o BD
            entityManager.getTransaction().begin();
            //indico o que quero persistir no meu BD
            entityManager.persist(entity);
            //faço a confirmação da transação de fato registrando a informação no BD
            entityManager.getTransaction().commit();
        } catch (Exception e){
            //Em caso de erro conseguimos desfazer o que estava em andamento
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            //caso a transação não esteja ativa no momento do erro imprimimos uma mensagem de erro e repassamos o erro para quem chamou o save
            System.err.println("Erro ao salvar: "+e.getMessage());
            throw new RuntimeException("Falha ao salvar", e);
        } finally{
            //fecha a via que foi aberta no tunel
            if(entityManager != null && entityManager.isOpen()){
                entityManager.close();
            }
            
        }
        //dando tudo certo eu retorno os dados da propria entidde que foi salva       
        return entity;
    }

    @Override
    public Entity update(Entity entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch(Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            //caso a transação não esteja ativa no momento do erro imprimimos uma mensagem de erro e repassamos o erro para quem chamou o save
            System.err.println("Erro ao atualizar: "+e.getMessage());
            throw new RuntimeException("Falha ao atualizar", e);
        }finally{
            //fecha a via que foi aberta no tunel
            if(entityManager != null && entityManager.isOpen()){
                entityManager.close();
            }
            
        }
        
        return entity;
    }

    @Override
    public void delete(Entity entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
            
        try{
            entityManager.getTransaction().begin();
            
            Entity entityToRemove = (Entity) entityManager.find(getTypeClass(), entity.getId());
            
            
            if (entityToRemove != null) { // Verifica se a entidade foi encontrada no BD
            
                entityManager.remove(entityToRemove); // `entityToRemove` está MANAGED
                entityManager.getTransaction().commit(); // Confirma a transação
                System.out.println("Entidade com ID " + entityToRemove.getId() + " removida com sucesso.");
        } else {
            // Se a entidade não foi encontrada, não há o que remover.
            // É importante fazer rollback se a transação foi iniciada e nada foi feito.
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.out.println("Entidade com ID " + entity.getId() + " não encontrada para remoção.");
        }
        } catch (Exception e) {
            // Em caso de erro, desfaz a transação.
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Erro ao excluir: " + e.getMessage());
            throw new RuntimeException("Falha ao excluir", e); // Relança a exceção
        } finally {
            //Fecha a via
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
   
    }

    @Override
    public List<Entity> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        //instancio um objeto para guardar minha lista
        List<Entity> result = null;
        try{
            //Cria uma consulta JPQL e irá retornar uma lista das entidades da tabela buscada
            // "c" é o alias da entidade na JPQL, "id" é o atributo de ID da sua entidade Curso.
            result = (List<Entity>) entityManager.createQuery("from " + getTypeClass().getName() + " c ORDER BY c.id ASC", getTypeClass()).getResultList();
        } catch (Exception e){
            System.err.println("Erro ao realizar busca: "+e.getMessage());
            throw new RuntimeException("Falha ao realizar busca", e);
        }finally{
            //fecha a via que foi aberta no tunel
            if(entityManager != null && entityManager.isOpen()){
                entityManager.close();
            }
            
        }
        return result;
    }

    @Override
    public Entity findById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        //Instancio um objeto para atribuir oq eu encontrar
        Entity entidadeEncontrada = null;
        try{
            entidadeEncontrada = (Entity) entityManager.find(getTypeClass(), id);
            //Caso não seja encontrado nada, imprimo no output uma msg
            if(entidadeEncontrada == null){
                System.out.println("Entidade com ID "+ id + "nao encontrada");
            }
        }catch(Exception e){
            throw new RuntimeException("Falha ao realizar busca", e);
        }finally{
            //fecha a via que foi aberta no tunel
            if(entityManager != null && entityManager.isOpen()){
                entityManager.close();
            }
            
        }
        return entidadeEncontrada;
    }
    
    /*Um metodo que serve para que os metodos genericos acima saibam com qual tipo de entidade especifica 
    eles estão lidando em tempo de execução, assim não preciso especificar a classe em cada chamada de método
    */
    protected Class<?> getTypeClass(){
        Class<?> clazz = (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        
        return clazz;
    }
}
