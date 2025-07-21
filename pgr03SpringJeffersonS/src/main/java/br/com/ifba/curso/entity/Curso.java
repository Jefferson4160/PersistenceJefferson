/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.curso.entity;

import br.com.ifba.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "curso")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Curso extends PersistenceEntity {
    //Impede que o BD salve um curso sem nome
    @Column(name = "nome", nullable = false)
    private String nome;
    //como o nome é composto no BD é criado com _ e colocamos igual na anotação. não pode ser vazio e deve ser unico
    @Column(name = "codigo_curso", nullable = false, unique = true, length = 255)
    private String codigoCurso;
    @Column(name = "ativo")
    private boolean ativo;
    
    //Mantenho somente esse construtor especifico
    // Para usar no delete e no findById
    public Curso(Long id) {
        super.setId(id); // Assume que setId é herdado de PersistenceEntity
    }
    //getters e setters removidos
    
}
