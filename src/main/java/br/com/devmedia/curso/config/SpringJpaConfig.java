package br.com.devmedia.curso.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//classe de integração entre Spring e o Hibernate
//indicar que essa classe vai ser de configuração
@Configuration

// fala para Spring gerenciar as transações da aplicação
@EnableTransactionManagement
public class SpringJpaConfig {

	// configração da configuração do banco de dados quando utiliza o Spring precisa marcar com um @Bean gerenciavel pelo Spring por isso usa essa anotação
	@Bean
	public DataSource dataSource() {
		//essa classe é propria do SpringFramework para criar um DataSoure com as informações de conexão ao banco de dados 
		// é uma classe para ser usada somente para teste ou desenvolvimento nunca em produção
		// se for levar a aplicação para produção o ideal é trabalhar com um pool de conexões
		DriverManagerDataSource ds = new DriverManagerDataSource();
		// qual driver de conexão estamos usando 
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		// adiciona a Url do banco de dados caso ele não existir ele cria
		ds.setUrl("jdbc:mysql://localhost:3306/test?createDatabaseIfNotExist=true&useSSL=false");
		ds.setUsername("root");
		ds.setPassword("123456");
		return ds;
	}

	// responsável por criar um objeto EntityManagerFactory 
	// no argumento tem objeto dataSource a partir da configuração do dataSource();
	@Bean
	public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
		// precisa indicar uma classe para ele cria um objeto EntityManagerFactory, LocalContainerEntityManagerFactoryBean é a classe propria do Springframework e vai criar o recurso do EntityManagerFactory
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		// informa o dataSource 
		factory.setDataSource(dataSource);
		// pacote que contém o mapeamento ou as classes que está mapeadas pela JPA
		factory.setPackagesToScan("br.com.devmedia.curso.domain");
		//qual framework ORM estamos utilizando, nessa caso adicionou a instancia HibernateJpaVendorAdapter pois está usando o hibernate
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		// propriedades adicionais vai incluir na configração, criou um metodo a parte
		factory.setJpaProperties(jpaProperties());
		// para fechar a configuração realizou no factory
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	// recebo como argumento o EntityManagerFactory que o Spring criou do metodo acima 
	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory factory) {

		JpaTransactionManager tx = new JpaTransactionManager();
		// adicionar o factory 
		tx.setEntityManagerFactory(factory);
		//para informar o dialeto do banco de dados, passa a classe do JPA que o Spring a partir dessa classe consegue identificar o banco qu está utilizando
		tx.setJpaDialect(new HibernateJpaDialect());
		return tx;
	}

	// é um metodo comum não precisa ser gerenciado pelo Spring 
	private Properties jpaProperties() {
		Properties props = new Properties();
		// show_sql como true para mostrar no console mostrar o SQL
		props.setProperty("hibernate.show_sql", "true");
		// format_sql como true para formatar o SQL que é exibido no console 
		props.setProperty("hibernate.format_sql", "true");
		// quando tem uma entidade mapeada, a entidade modifique ou crie a tabela lá no banco de dados conforme o mapeamento tem dela
		props.setProperty("hibernate.hbm2ddl.auto", "update");
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		return props;
	}

}
