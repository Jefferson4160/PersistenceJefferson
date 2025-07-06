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
    
    //O EntityManager é a interface principal do JPA para operações de persistência
    protected static EntityManager entityManager;
    //Um bloco estatico de inicialização é executado uma unica vez quando a classe genericDao é carregada pela JVM
    static{
        try{
            //Cria a EntityManagerFactory que é um objeto criado somente uma vez
            //Ele q irá ler o persistence.xml
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("gerenciamento_curso");
            //Cria uma instancia de EntityManager e será utilizso para todas operalções do BD
            entityManager = factory.createEntityManager(); 
        } catch (Exception e){
            //Aqui na mensagem de erro o getMessage retorna uma mensagem curta descrevendo o prq do erro
            System.err.println("Erro fatal ao inicializar JPA no GenericDao: "+e.getMessage());
            //O sistema é encerrado pois sem o EntityManagerFactory o programa não consegue interagir com o BD
            System.exit(1);
        }
        
    }

    @Override
    public Entity save(Entity entity) {
        //Inicia uma transação com o BD
        entityManager.getTransaction().begin();
        try{
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
        }
        //dando tudo certo eu retorno os dados da propria entidde que foi salva       
        return entity;
    }

    @Override
    public Entity update(Entity entity) {
        entityManager.getTransaction().begin();
        try{
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch(Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            //caso a transação não esteja ativa no momento do erro imprimimos uma mensagem de erro e repassamos o erro para quem chamou o save
            System.err.println("Erro ao atualizar: "+e.getMessage());
            throw new RuntimeException("Falha ao atualizar", e);
        }
        
        return entity;
    }

    @Override
    public void delete(Entity entity) {
        //Antes de remover tenho que garantir que a entity de fato esta no meu BD
        entity = findById(entity.getId());
        
        if(entity != null){
            entityManager.getTransaction().begin();
            try{
                entityManager.remove(entity); //faz a exclusão
                entityManager.getTransaction().commit();
            } catch(Exception e){
                if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
                }
                //caso a transação não esteja ativa no momento do erro imprimimos uma mensagem de erro e repassamos o erro para quem chamou o save
                System.err.println("Erro ao excluir: "+e.getMessage());
                throw new RuntimeException("Falha ao excluir", e);
            }
        }else{
            System.out.println("Entidade com ID" + entity.getId() + "nao encontrada.");
        }
        
        
    }

    @Override
    public List<Entity> findAll() {
        //instancio um objeto para guardar minha lista
        List<Entity> result = null;
        try{
            //Cria uma consulta JPQL e irá retornar uma lista das entidades da tabela buscada
            result = entityManager.createQuery(("from " + getTypeClass().getName())).getResultList();
        } catch (Exception e){
            System.err.println("Erro ao realizar busca: "+e.getMessage());
            throw new RuntimeException("Falha ao realizar busca", e);
        }
        return result;
    }

    @Override
    public Entity findById(Long id) {
        //Instancio um objeto para atribuir oq eu encontrar
        Entity entidadeEncontrada = null;
        try{
            entidadeEncontrada = (Entity) entityManager.find(getTypeClass(), id);
            //Caso não seja encontrado nada, imprimo no output uma msg
            if(entidadeEncontrada == null){
                System.out.println("Entidade com ID "+ id + "nao encontrada");
            }
        }catch(Exception e){
            
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
