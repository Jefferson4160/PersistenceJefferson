/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.save;

import br.com.ifba.curso.entity.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author Jefferson S
 */
public class CursoSave {
    
    //Para que o JPA leia o persistence.xml precisamos de um EntityManagerFactory
    private final static EntityManagerFactory entityManagerFactory = 
            Persistence.createEntityManagerFactory("gerenciamento_curso");
    //Para se comunicar com o JPA precisamos de um objeto do tipo EntityManager
    private final static EntityManager entityManager = 
            entityManagerFactory.createEntityManager();
    
    public static void main(String[] args){
        Curso curso = new Curso();
        //O id não precisa pois foi definido como auto incremento
        curso.setNome("Analise e Desenvolvimento de Sistemas");
        curso.setCodigoCurso("ADS");
        curso.setAtivo(true);
        
        //Processo para salvar
        //Abre transação
        entityManager.getTransaction().begin();
        //Digo qual objeto quero persistir
        entityManager.persist(curso);
        //Envio o objeto
        entityManager.getTransaction().commit();
        
        //Fechando os recursos
        entityManager.close();
        entityManagerFactory.close();
    }
    
}
