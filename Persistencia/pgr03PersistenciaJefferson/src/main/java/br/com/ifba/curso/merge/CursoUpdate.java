/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.merge;

import br.com.ifba.curso.entity.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author Jefferson S
 */
public class CursoUpdate {
    //Para que o JPA leia o persistence.xml precisamos de um EntityManagerFactory
    private final static EntityManagerFactory entityManagerFactory = 
            Persistence.createEntityManagerFactory("gerenciamento_curso");
    //Para se comunicar com o JPA precisamos de um objeto do tipo EntityManager
    private final static EntityManager entityManager = 
            entityManagerFactory.createEntityManager();
    
    public static void main(String[] ags){
        //Abro a conecção
        entityManager.getTransaction().begin();
        
        //Faço uma busca
        Curso cursoEncontrado = entityManager.find(Curso.class, 21);
        
        //Após encontrar o curso em questão posso atualizar apenas a coluna que quero sem me preocupar com um update vazio
        String status = "Inativo";
        if(cursoEncontrado.isAtivo()){
            status = "Ativo";
        }
        System.out.println("\n\n##############################\n\nCurso antes da atualizacao\n\n");
        System.out.println("Curso: "+cursoEncontrado.getNome()+
                    "\nCodigo: "+cursoEncontrado.getCodigoCurso()+
                    "\nStatus: "+status);
        System.out.println("\n\n##############################\n\n");
        
        //Informações que quero alterar
        //cursoEncontrado.setNome("Engenharia de Software");
        cursoEncontrado.setCodigoCurso("EGS");
        
        if(cursoEncontrado.isAtivo()){
            status = "Ativo";
        }
        System.out.println("\n\n##############################\n\nCurso apos a atualizacao\n\n");
        System.out.println("Curso: "+cursoEncontrado.getNome()+
                    "\nCodigo: "+cursoEncontrado.getCodigoCurso()+
                    "\nStatus: "+status);
        System.out.println("\n\n##############################\n\n");
        
        //Envio tudo para o BD
        entityManager.getTransaction().commit();
        
        //Fecho os recursos
        entityManager.close();
        entityManagerFactory.close();
    }
    
}
