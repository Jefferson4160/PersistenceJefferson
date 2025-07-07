/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.service;

import br.com.ifba.curso.dao.CursoDao;
import br.com.ifba.curso.dao.CursoIDao;
import br.com.ifba.curso.entity.Curso;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class CursoService implements CursoIService {
    
    //Objeto quue vou usar
    private final CursoIDao cursoDao = new CursoDao();
    
    //Metodos do objeto em questão
    @Override
    public Curso save(Curso curso) throws RuntimeException{
        if(curso == null){
            throw new RuntimeException("Dados do curso nao preenchidos");
        }else if(curso.getId() != null){
            throw new RuntimeException("Curso ja existe no Banco de Dados");
        }
        if(curso.getNome() == null){
            //O IllegalArgumentExpetion é uma forma mais especifica do RuntimeExeption
            throw new IllegalArgumentException("Nome do curso não pode ser vazio.");
        }
        if(curso.getCodigoCurso() == null){
            throw new IllegalArgumentException("Codigo do curso não pode ser vazio.");
        }
        
        try{
            //Delega o trabalho de salvar o curso ao metodo do DAO
            return cursoDao.save(curso);
        }catch (RuntimeException e){
            //Dessa forma lanço o erro com a sua mensagem mais especifica com o e.getMessage e mantenho o histórico passando o "e"
            throw new RuntimeException("Erro no service ao salvar o curso: "+e.getMessage(), e);
        }
        
    }

    @Override
    public Curso update(Curso curso) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Curso curso) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Curso> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Curso findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
