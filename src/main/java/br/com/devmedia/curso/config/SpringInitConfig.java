package br.com.devmedia.curso.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//Essa classe vai funcionar mais ou menos como fosse a class web.xml usando ela não precisa do web.xml
public class SpringInitConfig   extends AbstractAnnotationConfigDispatcherServletInitializer{
	
	// adiciona a classe raiz, está dizendo para o Spring que quando o servidor iniciar, ele vai subir a aplicação e essa classe vai ser a primeira configuração da aplicação que ele tem que carregar
	// quando o tomcat for startado essa configuração é startada junto então o Spring  vai configurar o ComponentScan
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {RootConfig.class};
	}

	// quem é a classe que contém a parte de servlets para nós
	// agora ele sabe quando for trabalhar com Servlet ele vai respeitar o que tem de configuração nessa classe
	// 
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return  new Class[] {SpringMvcConfig.class};
	}

	// esse vai ser o mapeamento dos servlets sempre que a aplicação encontrar na URL uma /, ela vai saber que esta lidando com um servlet
	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}

}
