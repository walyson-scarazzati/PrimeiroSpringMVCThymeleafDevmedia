package br.com.devmedia.curso.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
 
//essa notação diz ao Spring que essa é uma classe de configuração
@Configuration

//serve para dizer ao Spring Framework onde ele deve procurar as classes que ele vai gerenciar 
//fazendo das seguintes maneiras pode passar um array onde cada um dos pacotes que deseja escanear ou 
//um array de classes, onde você informa uma a uma as classes que devem ser escaneadas, 
//mas a maneira mais fácil segundo o autor é adicionar o pacote base da aplicação e a partir daí o Spring escaneia tudo 
@ComponentScan("br.com.devmedia.curso")

// fala para pro Spring que deseja usar o Spring MVC
@EnableWebMvc
public class RootConfig {

}
