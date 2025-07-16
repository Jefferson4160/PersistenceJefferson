/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.ifba.curso.controller;

import br.com.ifba.curso.entity.Curso;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface CursoIController {
    
    Curso save(Curso curso);
    
    Curso update(Curso curso);
    
    void delete(Curso curso);
    
    Curso findById(Long id);
    
    List<Curso> findAll();
    
    List<Curso> findByName(String name);
    
}
