package br.com.devmedia.curso.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
 
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.devmedia.curso.domain.TipoSexo;
import br.com.devmedia.curso.domain.Usuario;

// sempre que trabalha com armazenamento, persistencia usa essa anotação
@Repository

// assim todos outros metodos que não possuem o anotação @Transactional dentro da classe vão estar anotados indiretamente
// quando fez essa anotação no getTodos marcou o atributo readOnly como true, pois por padrão é false que significa por padrão não é apenas leitura, assim não precisa adicionar o atributo
@Transactional
public class UsuarioDaoImpl implements UsuarioDao{
	
	// injeta objeto EntityManager para que tenha acesso ao metodos da JPA para operações de CRUD faz essa injeção atraves da @PersistenceContext
	// @PersistenceContext obter atraves da fabrica de objetos EntityManager, criou lá SpringJpaConfig objeto EntityManager sempre que precisar desse objeto
	@PersistenceContext
    private EntityManager entityManager;
	
	// esse é um metodo de escrita
	@Override
    public void salvar(Usuario usuario) {
		// senão trabalhasse com o Spring somente JPA seria assim
		// abrir a transação do entityManager
	//	entityManager.getTransaction().begin();
		//objeto que iriamos persistir no banco de dados
	//	entityManager.persist(usuario);
		//comitar a transação
	//	entityManager.getTransaction().commit();
		//fechar o entityManager
	//	entityManager.close();
		
		
		
		//transação gerenciada pelo Spring, somente isso usando Spring
        entityManager.persist(usuario); 
    }
 
    @Override
    public void editar(Usuario usuario) {
        entityManager.merge(usuario);       
    }
 
    @Override
    public void excluir(Long id) {
    	// precisa do objeto Usuario para remover usa getReference para isso, com isso busca objeto que quero excluir no banco transforma em um objeto persistente dai posso remover objeto da base de dados
        entityManager.remove(entityManager.getReference(Usuario.class, id));        
    }
 
    @Transactional(readOnly = true)
    @Override
    public Usuario getId(Long id) {
        String jpql = "from Usuario u where u.id = :id";
        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
        // passa o nome do parametro que passar  depois o valor que passado por parametro
        query.setParameter("id", id);
        // devolve somente 1 usuario
        return query.getSingleResult();
    }
    
   // Embora a HQL seja a linguagem padrão de consultas do Hibernate, a JPA, por ser uma especificação, possui uma linguagem de consultas 
   // própria, que deve ser processada por qualquer framework ORM que siga a especificação JPA. Essa linguagem é chamada JPQL 
   // e foi baseada na HQL do Hibernate. A principal diferença entre a HQL e a JPQL está na instrução Select. 
   // Na HQL a instrução é opcional, já na JPQL essa instrução é obrigatória.
    // readOnly = true essa propriedade vai dizer ao gerenciamento de transações do SpringFramework que esse metodo é apenas um metodo de leitura 
    // geralmente usa em metodos de consulta/leitura
    @Transactional(readOnly = true)
    @Override
    public List<Usuario> getTodos() {
        //na consulta usa linguagem JPQL que é a linguagem padrão de consultas da JPA
        String jpql = "from Usuario u";
        // createQuery recebe a jpql e qual tipo de objeto está trabalhando  
        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
        //retorna objeto java.util.List contendo todas entidades que foram retornadas a partir da tabela usuarios pela consulta JPQL
        return query.getResultList();
    }
 
    @Transactional(readOnly = true)
    @Override
    public List<Usuario> getBySexo(TipoSexo sexo) {
        String jpql = "from Usuario u where u.sexo = :sexo";
        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
        query.setParameter("sexo", sexo);
        return query.getResultList();
    }
 
    @Transactional(readOnly = true)
    @Override
    public List<Usuario> getByNome(String nome) {
        String jpql = "from Usuario u where u.nome like :nome or u.sobrenome like :sobrenome";
        TypedQuery<Usuario> query = entityManager.createQuery(jpql, Usuario.class);
      //  símbolos de porcentagem (%).
      // Este símbolo é chamado de coringa em consultas e faz com que a consulta localize qualquer registro que contenha como 
      // parte do nome ou do sobrenome o valor da variável de argumento.
        query.setParameter("nome", "%"+nome+"%");
        query.setParameter("sobrenome", "%"+nome+"%");
        return query.getResultList();
    }
	
	// Conteúdo antes de usar JPA
	
	
	/*
	 * private static List<Usuario> us;
	 * 
	 * public UsuarioDaoImpl() { createUserList(); }
	 * 
	 * private List<Usuario> createUserList() { if (us == null) { us = new
	 * LinkedList<>(); us.add(new Usuario(System.currentTimeMillis() + 1L, "Ana",
	 * "da Silva", LocalDate.of(1992, 5, 10), TipoSexo.FEMININO)); us.add(new
	 * Usuario(System.currentTimeMillis() + 2L, "Luiz", "dos Santos",
	 * LocalDate.of(1990, 8, 11), TipoSexo.MASCULINO)); us.add(new
	 * Usuario(System.currentTimeMillis() + 3L, "Mariana", "Mello",
	 * LocalDate.of(1988, 9, 17), TipoSexo.FEMININO)); us.add(new
	 * Usuario(System.currentTimeMillis() + 4L, "Caren", "Pereira")); us.add(new
	 * Usuario(System.currentTimeMillis() + 5L, "Sonia", "Fagundes")); us.add(new
	 * Usuario(System.currentTimeMillis() + 6L, "Norberto", "de Souza")); } return
	 * us; }
	 * 
	 * @Override public void salvar(Usuario usuario) {
	 * usuario.setId(System.currentTimeMillis()); us.add(usuario); }
	 * 
	 * @Override public void editar(Usuario usuario) { us.stream() .filter((u) ->
	 * u.getId().equals(usuario.getId())) .forEach((u) -> {
	 * u.setNome(usuario.getNome()); u.setSobrenome(usuario.getSobrenome());
	 * u.setDtNascimento(usuario.getDtNascimento()); u.setSexo(usuario.getSexo());
	 * }); }
	 * 
	 * @Override public void excluir(Long id) { us.removeIf((u) ->
	 * u.getId().equals(id)); }
	 * 
	 * @Override public Usuario getId(Long id) { return us.stream() .filter((u) ->
	 * u.getId().equals(id)) .collect(Collectors.toList()).get(0); }
	 * 
	 * @Override public List<Usuario> getTodos() { return us; }
	 */

}
