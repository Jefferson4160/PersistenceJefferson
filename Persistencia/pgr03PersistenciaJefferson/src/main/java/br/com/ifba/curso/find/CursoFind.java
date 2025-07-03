/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.find;

import br.com.ifba.curso.entity.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author Jefferson S
 */
public class CursoFind {
    //Para que o JPA leia o persistence.xml precisamos de um EntityManagerFactory
    private final static EntityManagerFactory entityManagerFactory = 
            Persistence.createEntityManagerFactory("gerenciamento_curso");
    //Para se comunicar com o JPA precisamos de um objeto do tipo EntityManager
    private final static EntityManager entityManager = 
            entityManagerFactory.createEntityManager();
    
    public static void main(String[] ags){
        //Buscando um curso
        entityManager.getTransaction().begin();
        Curso cursoEncontrado = entityManager.find(Curso.class, 21);
        String status = "Inativo";
        if(cursoEncontrado.isAtivo()){
            status = "Ativo";
        }
        System.out.println("\n\n##############################\n\n");
        System.out.println("Curso: "+cursoEncontrado.getNome()+
                    "\nCodigo: "+cursoEncontrado.getCodigoCurso()+
                    "\nStatus: "+status);
         System.out.println("\n\n##############################\n\n");
        //Fechando os recursos
        entityManager.close();
        entityManagerFactory.close();
    }
    
        
}
