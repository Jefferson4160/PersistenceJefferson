/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.entity;

import br.com.ifba.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "curso")
public class Curso extends PersistenceEntity {
    //Impede que o BD salve um curso sem nome
    @Column(name = "nome", nullable = false)
    private String nome;
    //como o nome é composto no BD é criado com _ e colocamos igual na anotação. não pode ser vazio e deve ser unico
    @Column(name = "codigo_curso", nullable = false, unique = true, length = 255)
    private String codigoCurso;
    @Column(name = "ativo")
    private boolean ativo;
    
    public Curso(){
        
    }
     // Construtor completo
    public Curso(String nome, String codigoCurso, boolean ativo) {
        this.nome = nome;
        this.codigoCurso = codigoCurso;
        this.ativo = ativo;
    }

    // Para usar no delete e no findById
    public Curso(Long id) {
        super.setId(id); // Assume que setId é herdado de PersistenceEntity
    }
    //getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    
    
}
