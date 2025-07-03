/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.remove;

import br.com.ifba.curso.entity.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author Jefferson S
 */
public class CursoDelete {
    //Para que o JPA leia o persistence.xml precisamos de um EntityManagerFactory
    private final static EntityManagerFactory entityManagerFactory = 
            Persistence.createEntityManagerFactory("gerenciamento_curso");
    //Para se comunicar com o JPA precisamos de um objeto do tipo EntityManager
    private final static EntityManager entityManager = 
            entityManagerFactory.createEntityManager();
    
    public static void main(String[] ags){
        
        //Realizamos a busca com o find
        Curso cursoEncontrado = entityManager.find(Curso.class, 22);
        
        entityManager.getTransaction().begin();
        entityManager.remove(cursoEncontrado);
        entityManager.getTransaction().commit();
        
        //Fechando os recursos
        entityManager.close();
        entityManagerFactory.close();
    }
    
}
