/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.ifba.curso.view;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import br.com.ifba.curso.controller.CursoIController;

import br.com.ifba.curso.entity.Curso;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import br.com.ifba.curso.view.renderer.Redenrizador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author ADMIN
 */
@Component
public class CursoListar extends javax.swing.JFrame {
    
    private DefaultTableModel modeloTabelaProdutos;
    @Autowired
    private CursoIController cursoController;
    /**
     * Creates new form CursoListar
     */
    public CursoListar() {
        initComponents();       
   
    }
    
    @PostConstruct
    public void init(){
            //Configuração do modelo de tabela
            //Definindo o nome das colunas
            String[] nomeColunas = {"ID","Curso","Cod.","Ativo","Remover","Editar"};
            //INstanciando um objeto da classe DefaulltTableModel que declaramos la em cima
            //passo os nomes das colunas e defino que começa com zero linhas
            modeloTabelaProdutos = new DefaultTableModel(nomeColunas,5);

            //Agora aqui eu indico que minha tabela de produtos vai seguir o modelo definido 
            tblCursos.setModel(modeloTabelaProdutos); 


            // Crie os objetos ImageIcon para suas imagens
            // Caminho para as imagens: "/images/nome_da_sua_imagem.png"
            // As imagens devem estar em src/main/resources/images
            ImageIcon iconeLixeira = new ImageIcon(getClass().getResource("/images/icons8-lixeira-25.png"));
            ImageIcon iconeLapis = new ImageIcon(getClass().getResource("/images/icons8-editar-25.png"));

            // Aplique o ButtonRenderer às colunas "Remover" e "Editar"
            // (Assumindo que "Remover" é a coluna de índice 4 e "Editar" é a de índice 5)
            tblCursos.getColumn("Remover").setCellRenderer(new Redenrizador(iconeLixeira));
            tblCursos.getColumn("Editar").setCellRenderer(new Redenrizador(iconeLapis));

            // Opcional: Aumente a altura da linha para o ícone caber melhor
            tblCursos.setRowHeight(30);

            tblCursos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tblCursos.columnAtPoint(e.getPoint());
                int row = tblCursos.rowAtPoint(e.getPoint());

                if (row >= 0 && column >= 0) {
                    // Para o teste visual, podemos usar a linha para identificar o item.
                    // Long cursoId = (Long) jTableCursos.getModel().getValueAt(row, 0); // Para pegar o ID do BD

                    String clickedColumnName = tblCursos.getColumnName(column);

                    if (clickedColumnName.equals("Remover")) {
                        //Obtenho o Id da linha clicada
                        Long cursoId = (Long) modeloTabelaProdutos.getValueAt(row, 0);

                        System.out.println("Ícone REMOVER clicado na linha: " + row);
                        int confirm = JOptionPane.showConfirmDialog(CursoListar.this,
                                "Tem certeza que deseja excluir o item de ID " + cursoId + "?",
                                "Confirmar Exclusão",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {


                            try{
                                //Uso o construtor quem tem somente o id para passar para o controller
                                Curso cursoParaRemover = new Curso(cursoId);
                                //Chamo o metodo do controller
                                cursoController.delete(cursoParaRemover);

                                modeloTabelaProdutos.removeRow(row);//tiro a linha do item excluido
                                JOptionPane.showMessageDialog(rootPane, "Iten excluido com sucesso");
                                System.out.println("Usuário confirmou exclusão da linha: " + (row + 1));
                            }catch (RuntimeException ex){
                                JOptionPane.showMessageDialog(rootPane, "Erro ao excluir: " + ex.getMessage());
                                ex.printStackTrace();
                                System.out.println("Usuário tentou excluir, mas houve erro na linha: " + (row + 1));
                            }

                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Exclusão cancelada");
                            System.out.println("Usuário CANCELOU exclusão da linha: " + (row + 1));
                        }
                    } else if (clickedColumnName.equals("Editar")) {
                        System.out.println("Ícone EDITAR clicado na linha: " + row);
                        Long cursoId = (Long) tblCursos.getModel().getValueAt(row, 0);//pega o id da linha clicada
                        // CHAME O MÉTODO PARA ABRIR A TELA EditarCurso AQUI
                        abrirTelaEdicao(cursoId); //abro a tela de edição passando o numero da linha

                    }
                }
            }
            
        });
            // Listener para o campo de busca (textProcurar) para buscar ao pressionar Enter
        textProcurar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarBusca(); // Chama o método para realizar a busca
            }
        });

        // MouseListener para o campo de busca (textProcurar) para apagar "Procurar..." ao clicar
        textProcurar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (textProcurar.getText().equals("Procurar...")) {
                    textProcurar.setText("");
                }
            }
        });
            carregarCursosNaTabela();       
        
    }
    
    
    
    private void carregarCursosNaTabela() {
    // Limpa todas as linhas existentes na tabela antes de adicionar as novas
    modeloTabelaProdutos.setRowCount(0);

    try {
        // Agora faço a conecção com o controller e ele chama o resto
        List<Curso> cursos = cursoController.findAll();

        // Itera sobre a lista de cursos e adiciona cada um como uma nova linha na tabela
        if (cursos != null && !cursos.isEmpty()) {
            for (Curso curso : cursos) {
                modeloTabelaProdutos.addRow(new Object[]{
                    curso.getId(),
                    curso.getNome(),
                    curso.getCodigoCurso(),
                    curso.isAtivo(),
                    "Remover", // Placeholder para o renderizador de imagem
                    "Editar"   // Placeholder para o renderizador de imagem
                });
            }
        } else {
            System.out.println("Nenhum curso encontrado no banco de dados.");
        }

    } catch (RuntimeException e) { // Captura a RuntimeException relançada pelo DAO
        JOptionPane.showMessageDialog(this, "Erro ao carregar cursos: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    
    private void realizarBusca() {
    String termoBusca = textProcurar.getText();
    modeloTabelaProdutos.setRowCount(0); // Limpa a tabela antes da nova busca

    if (termoBusca.isEmpty() || termoBusca.equals("Procurar...")) {
        carregarCursosNaTabela(); // Se o campo de busca estiver vazio ou com placeholder, recarrega todos
        return;
    }

    try {
        // Tenta converter o texto para Long (ID)
        Long idBusca = Long.parseLong(termoBusca);
        Curso cursoEncontrado = cursoController.findById(idBusca); // Usa o Controller para buscar por ID

        if (cursoEncontrado != null) {
            // Se encontrou por ID, adiciona apenas esse curso à tabela
            modeloTabelaProdutos.addRow(new Object[]{
                cursoEncontrado.getId(),
                cursoEncontrado.getNome(),
                cursoEncontrado.getCodigoCurso(),
                cursoEncontrado.isAtivo(),
                "Remover",
                "Editar"
            });
            System.out.println("Curso encontrado por ID: " + cursoEncontrado.getNome());
        } else {
            // Se não encontrou por ID, exibe mensagem
            JOptionPane.showMessageDialog(this, "Curso com ID " + idBusca + " não encontrado.", "Busca por ID", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Nenhum curso encontrado com o ID: " + idBusca);
        }

    } catch (NumberFormatException ex) {
        // Se o texto não é um número válido, tenta buscar por nome
        System.out.println("Termo de busca não é um ID, tentando buscar por nome...");
        try {
            // Usa o Controller para buscar por nome
            List<Curso> cursosEncontrados = cursoController.findByNameIgnoreCase(termoBusca);

            if (cursosEncontrados != null && !cursosEncontrados.isEmpty()) {
                for (Curso curso : cursosEncontrados) {
                    modeloTabelaProdutos.addRow(new Object[]{
                        curso.getId(),
                        curso.getNome(),
                        curso.getCodigoCurso(),
                        curso.isAtivo(),
                        "Remover",
                        "Editar"
                    });
                }
                System.out.println(cursosEncontrados.size() + " cursos encontrados por nome.");
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum curso encontrado com o nome '" + termoBusca + "'.", "Busca por Nome", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Nenhum curso encontrado com o nome: " + termoBusca);
            }
        } catch (RuntimeException eNome) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar cursos por nome: " + eNome.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            eNome.printStackTrace();
        }
    } catch (RuntimeException ex) {
        // Captura outros erros da camada de serviço/DAO durante a busca
        JOptionPane.showMessageDialog(this, "Erro geral ao realizar a busca: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
    
    private void abrirTelaEdicao(Long idCurso) { // Agora o método recebe a linha clicada
        System.out.println("Chamando tela de edição para o Curso ID: " + idCurso);

        //aqui passo a referencia da tela pai; o modo de abertura; o idCurso e por fim o cursoContrtoller para injeção de dependencia
        EditarCurso telaEdicao = new EditarCurso(this, true, idCurso, this.cursoController); 

        telaEdicao.setLocationRelativeTo(this);
        telaEdicao.setVisible(true);
        System.out.println("Tela de Edição (EditarCurso) foi fechada.");
        carregarCursosNaTabela(); // Recarrega a tabela após o fechamento
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textProcurar = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCursos = new javax.swing.JTable();
        lblIcon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textProcurar.setText("Procurar...");

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-adicionar-25.png"))); // NOI18N
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        tblCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblCursos);

        lblIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-pesquisar-25.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(287, 287, 287))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(textProcurar)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // TODO add your handling code here:
        //Crio uma instancia da nova tela criada para exibi-la
        AdicionarCurso telaAdicao = new AdicionarCurso(this, true, this.cursoController);
        
        //dessa forma uso a posição relativa da janela em questão e abro a segunda em relação a ela
        telaAdicao.setLocationRelativeTo(this);
        //comando para tornar a janela visivel
        telaAdicao.setVisible(true);
        //atualiza a tabela
        carregarCursosNaTabela();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CursoListar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CursoListar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CursoListar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CursoListar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CursoListar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JTable tblCursos;
    private javax.swing.JTextField textProcurar;
    // End of variables declaration//GEN-END:variables
}
