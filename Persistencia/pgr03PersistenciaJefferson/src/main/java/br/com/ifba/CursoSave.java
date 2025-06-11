package br.com.ifba;

import br.com.ifba.curso.view.CursoListar;

public class CursoSave {

    public static void main(String[] args) {
        // Garante que a interface gráfica seja executada na thread de eventos do Swing.
        // Isso é uma boa prática e é necessário para evitar problemas de thread safety em GUIs Swing.
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Cria uma nova instância da sua tela CursoListar
                CursoListar telaListagem = new CursoListar();
                // Torna a tela visível
                telaListagem.setVisible(true);
            }
        });
        
    }
}