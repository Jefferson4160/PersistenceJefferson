/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.repository;

import br.com.ifba.curso.entity.Curso;
import java.util.List;                 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;            


@Repository // <--- Anotação Spring para indicar que é um Bean de Repositório
public interface CursoRepository extends JpaRepository<Curso, Long> {
    // JpaRepository<Tipo da Entidade, Tipo do ID>

    // --- Método de Busca por Nome (Find By Name) ---
    // O Spring Data JPA cria a query automaticamente com base no nome do método.
    // Ele buscará Cursos onde o atributo 'nome' corresponde ao 'nome' passado. 
    List<Curso> findByNomeContainingIgnoreCase(String nome); 

    
}