/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.service;

import br.com.ifba.curso.dao.CursoDao;
import br.com.ifba.curso.dao.CursoIDao;
import br.com.ifba.curso.entity.Curso;
import br.com.ifba.infrastructure.util.StringUtil;
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
        //chamo o metodo auxiliar e passo o objeto curso e como segundo parametro false, para indicar que n é update
        validarCurso(curso, false);
        
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
        //agora passo true pois é um update
        validarCurso(curso, true);
        //Verifico se o curso esta no BD
        Curso cursoExistente = cursoDao.findById(curso.getId());
        if(cursoExistente == null){
            throw new RuntimeException("Curso com ID"+curso.getId()+" nao encontrado");
        }
        
        cursoExistente.setNome(curso.getNome());
        cursoExistente.setCodigoCurso(curso.getCodigoCurso());
        cursoExistente.setAtivo(curso.isAtivo());
    
        try {
            return cursoDao.update(cursoExistente); // Chama o método update do DAO.
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro no service ao atualizar o curso: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Curso curso) {
        //Verifico se o curso esta no BD
        Curso cursoExistente = cursoDao.findById(curso.getId());
        if(cursoExistente == null){
            throw new RuntimeException("Curso com ID"+curso.getId()+" nao encontrado");
        }
        
        try {
            cursoDao.delete(cursoExistente);//faço a remoção
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro no service ao remover o curso: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Curso> findAll() {
        //simplesmente busco tudo que houver e retorno
        try {
            return cursoDao.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro no service ao listar todos os cursos: " + e.getMessage(), e);
        }
    }

    @Override
    public Curso findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do curso não pode ser nulo para busca.");
        }
        try {
            return cursoDao.findById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro no service ao buscar curso por ID: " + e.getMessage(), e);
        }
    }
    
    //Metodo auxiliar para validar o objeto curso
    private void validarCurso(Curso curso, boolean isUpdate){
        //Se curso é nulo não serve nem para save nem update
        if (curso == null) {
            throw new RuntimeException("Dados do curso não preenchidos.");
        }
        //Se for um update eu verifico se foi passado o ID
        if (isUpdate) {
            if (curso.getId() == null) {
                throw new IllegalArgumentException("ID do curso é obrigatório para atualização.");
            }
        } else { // Se vier para cá, foi chamado o metodo save
            if (curso.getId() != null) {
                // Se o ID não é nulo em uma operação de 'save', indica que o objeto já pode existir.
                throw new RuntimeException("Curso já existe no Banco de Dados.");
            }
        }
        
        //Aqui faço a verificação de cada campo
        if (StringUtil.isNullOrEmpty(curso.getNome())) {
            throw new IllegalArgumentException("Nome do curso não pode ser vazio.");
        }
        if (StringUtil.isNullOrEmpty(curso.getCodigoCurso())) {
            throw new IllegalArgumentException("Código do curso não pode ser vazio.");
        }
    }
}
