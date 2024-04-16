package br.com.devmedia.curso.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

//precisa desta anotação para dizer ao Spring que essa classe é um controller
@Controller
public class WelcomeController {
	// para identificar que queremos acessar esse metodo faz a anotação @RequestMapping junto com o atributo value onde diz qual path representa esse método
	// o atributo method vai definir qual protocolo HTTP que vai trabalhar com esse método
	// o GET é o método padrão de consulta do RequestMapping, caso não adicionar o atributo method automaticamente vai ser do tipo GET
	@RequestMapping(value = "/" , method = RequestMethod.GET)
	public String welcome() {
		
		// essa string é simplesmente o nome da página não precisa da extensão .jsp pois ela já foi configurada na classe MVC no método View Resolver e tambem tem o caminho até essa página configurado no prefixo
		//return "welcome";
		// redirecionar o acesso para controller "usuario" e não para página da lista  
		// o redirect é feito a partir do controller, então usa nome do controller ou caminho do controller
		// quando vai acessar a página  como tivesse trabalhando diretamente com Servelet um forward acessa diretamente a página basta ir no diretorio da pagina "user"
		return "redirect:/usuario/todos";
	}
	
	// ModelAndView é um objeto do Spring que enviar um objeto completo a partir dele 
	@RequestMapping(value = "/teste" , method = RequestMethod.GET)
	public ModelAndView teste() {
		// no construtor coloco a página que quero abrir
		ModelAndView view = new ModelAndView("welcome");
		
		// no primeiro parametro adiciono uma String com o nome da variavel que vou esperar receber lá na página nesse exemplo é "teste"
		// no segundo passa o valor que quero enviar para página
		view.addObject("teste","Olá, eu sou o Spring MVC.");
		
		// essa string é simplesmente o nome da página não precisa da extensão .jsp pois ela já foi configurada na classe MVC no método View Resolver e tambem tem o caminho até essa página configurado no prefixo
		return view;
	}

}
