package br.com.ifba;

import br.com.ifba.curso.view.CursoListar; 
import javax.swing.JFrame; 
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;




@SpringBootApplication //já inclui ComponentScan e AutoConfiguration
public class Pgr03springJeffersonS {

    public static void main(String[] args) {
        
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Pgr03springJeffersonS.class).headless(false).run(args);

        // Obter a tela CursoListar 
        CursoListar cursoListarFrame = context.getBean(CursoListar.class);
        cursoListarFrame.setVisible(true);

        // para fechar o contexto Spring ao fechar a janela
        cursoListarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Garante que a app feche
        cursoListarFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                context.close(); // Fecha o contexto Spring
                System.out.println("Contexto Spring Boot fechado.");
            }
        });
    }
}