/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.dao;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.infrastructure.dao.GenericDao;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jefferson S
 */
@Repository
public class CursoDao extends GenericDao<Curso> implements CursoIDao{
    
    @Override
    public List<Curso> findByName(String name) {
        List<Curso> result = null;
        try {
            // Acessa o entityManager injetado da superclasse GenericDao
            String jpql = "FROM " + getTypeClass().getName() + " c WHERE LOWER(c.nome) LIKE LOWER(:name) ORDER BY c.id ASC";

            TypedQuery<Curso> query = entityManager.createQuery(jpql, Curso.class); // Use Curso.class diretamente aqui
            query.setParameter("name", "%" + name + "%");

            result = query.getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar cursos por nome: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha ao buscar cursos por nome", e);
        }
        return result;
    }
}
