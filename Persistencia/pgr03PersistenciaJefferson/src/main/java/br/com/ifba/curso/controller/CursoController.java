/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.controller;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.curso.service.CursoIService;
import br.com.ifba.curso.service.CursoService;
import java.util.List;

/**
 *
 * @author Jefferson S
 */
public class CursoController implements CursoIController{
    
    // Faço a relação com o service, para fazer as chamadas
    private final CursoIService cursoService; 

    public CursoController() {
        // Instancia a implementação concreta do Serviço.
        this.cursoService = new CursoService();
    }

    // --- Métodos implementados da interface CursoIController ---
  
    @Override
    public Curso save(Curso curso) throws RuntimeException {
        try {
            // Delega a chamada para o Serviço, passando o objeto Curso recebido da View.
            return cursoService.save(curso);
        } catch (RuntimeException e) {
            throw e; // Relança a exceção
        }
    }

    
    @Override
    public Curso update(Curso curso) throws RuntimeException {
        try {
            // Delega a chamada para o Serviço, passando o objeto Curso.
            return cursoService.update(curso);
        } catch (RuntimeException e) {
            throw e; // Relança a exceção
        }
    }

    
    @Override
    public void delete(Curso curso) throws RuntimeException {
        try {
            // Delega a chamada para o Serviço.
            cursoService.delete(curso);
        } catch (RuntimeException e) {
            throw e; // Relança a exceção
        }
    }

   
    @Override
    public Curso findById(Long id) throws RuntimeException {
        try {
            // Delega a chamada para o Serviço.
            return cursoService.findById(id);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    
    @Override
    public List<Curso> findAll() throws RuntimeException {
        try {
            // Delega a chamada para o Serviço.
            return cursoService.findAll();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    
    
}
