package br.com.devmedia.curso.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import br.com.devmedia.curso.web.conversor.TipoSexoConverter;

// Para iniciar a configuração do Thymeleaf é necessario implementar interface ApplicationContextAware implementa ela por que 
// ela vai dar acesso ao um objeto que contém o contexto  da aplicação  esse contexto é importante para set ele em metodo da configuração
// referente ao Thymeleaf

//para dizer que essa classe é de configuração
@Configuration

public class SpringMvcConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	
	// para que possa ter acesso ao objeto de contexto da aplicação vai subscrever essse metodo da aplicação
	//tem na lista de argumentos o objeto applicationContext  
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// atribui o argumento com a variavel para poder ter no scopo da classe acesso a uma instancia de applicationContext
		this.applicationContext = applicationContext;
	}
	
	// inicia configuração do Thymeleaf para informar ao Spring MVC como ele vai lidar com Thymeleaf
	@Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        // insere o aplicationContext na configuração 
        templateResolver.setApplicationContext(applicationContext);
        //informando o diretorio base da aplicação que vai conter as páginas HTML
        templateResolver.setPrefix("/WEB-INF/views/");
        // tipo de página vai utilizar para o Spring MVC ou seja extensão da pagina
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        // tipo de código contem suas páginas
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // value true é padrão habilita um sistema de gerenciamento de cache do lado do cliente feito pelo Thyneleaf para melhorar a experiencia do usuario
        // quando trabalha com mode desenvolvimento é bom desabilitar esse 
        templateResolver.setCacheable(false);
        return templateResolver;
    }
 
	// informar as configurações que fez no templateResolver() para o núcleo da biblioteca do Thymeleaf junto a integração do Spring
    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // Aqui coloca no parametro o metodo anterior 
        templateEngine.setTemplateResolver(templateResolver());
        // Habilita para trabalhar com linguagens de expressões baseado em liguagens de expressão do Spring 
        templateEngine.setEnableSpringELCompiler(true);
        // Qual metodo da configuração que vai cuidar de arquivos de propriedades nesse caso é metodo messageSource()
        templateEngine.setTemplateEngineMessageSource(messageSource());
        // Adicionou o Dialeto de datas para cuidar desse tipo de dado
        templateEngine.addDialect(new Java8TimeDialect());
        return templateEngine;
    }
    
    // todas as classes que você cria utilizando as anotações do Spring no topo se
 	// tornam um bean e passar a ser gerenciadas
 	// pelo sistema de gestão de dependências do Spring
 	// quando cria um método e a precisa que esse método seja gerenciado pelo Spring
 	// a genta adiciona a ele uma notação
 	// que é a @Bean assim o Spring passa controlar esse método
 
    // é metodo que reune todas as configurações do templateResolver que foi incluida no templateEngine esse ultimo incluimos aqui
    // objetivo é ser usado pelos controllers do Spring para que possa lidar com resoluções de views 
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }
 
    // para carregar as mensagens de um arquivo de propriedades
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
 
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new TipoSexoConverter());
    }
 
    // fala para o Spring que quer trabalhar com arquivos estaticos
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/WEB-INF/resources/");
    }
	

}
