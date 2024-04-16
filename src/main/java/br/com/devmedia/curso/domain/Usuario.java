package br.com.devmedia.curso.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

// Realizando um mapeamento objeto relacional da classe usuario 

//quando trabalha com Spring MVC é obrigatório a ter na classe de domínio o construtor padrão padrão (sem parametros)
// mesmo que já declarou um construtor com parametros, pois o Spring MVC utiliza o construtor padrão (sem parametros)
// para trabalhar com o objeto de domínio na View

// implementa Serializable é uma boa pratica quando trabalhar com frameworks orm implementar isso na entidade


@Entity //para marcar essa classe como uma entidade gerenciada pela jpa ou pelo framework orm faz import do pacote javax.persistence

@Table(name= "usuarios") // coloca o nome da tabela que vai ter no banco de dados que representa essa classe que nesse caso é usuarios
public class Usuario implements Serializable {

	@Id // para identificar o atributo id como o atributo que vai personificar a chave primaria do banco de dados 
	@GeneratedValue(strategy = GenerationType.IDENTITY) // para fazer que a estrategia de geração de chaves no mysql será por autoincremento
	private Long id;

	// O formulario não pode enviar esse campo vazio e menor que 3 caracteres e maior que 50
	@Column(nullable = false, length = 50)   // nullable usa esse atributo como false para garantir que não serão aceitos valores nulos nessa coluna
	// o lenght é para o tamanho da coluna 
	@NotBlank
	@Size(min = 3, max = 50)
	private String nome;

	@Column(nullable = false, length = 50) 
	@NotBlank
	@Size(min = 3, max = 50, message = "Campo requerido entre {min} e {max} caracteres.")
	private String sobrenome;

	@Column(name = "data_nascimento", nullable = false) // propriedade name está referenciando o nome da coluna que terei no banco de dados, 
	// quando não utiliza a propriedade name está referenciando a sua coluna no banco de dados desde que ela contenha mesmo nome do atributo 
	
	@NotNull(message = "O campo 'data de nascimento' é requerido.")
	// faz a conversão de string para LocalDate
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dtNascimento;

	@Column(name = "tipo_sexo", nullable = false)
	// fala para JPA que está trabalhando com tipo enum, escolheu o tipo String por que quando for salvar no banco ele pega o nome da constante salva como String ou no caso um varchar no Mysql 
	// se usar o valor ordinal a order que você tem das sua constantes 0 ou 1
	@Enumerated(EnumType.STRING)
	private TipoSexo sexo;

	// somente esse mantém para JPA
	public Usuario() {
		super();
	}
	 
	    //quando usar o dao refente a JPA não precisa usar métodos construtores sobrecarregados
	    public Usuario(Long id, String nome, String sobrenome) {
	        super();
	        this.id = id;
	        this.nome = nome;
	        this.sobrenome = sobrenome;
	    }   
	     
	    public Usuario(Long id, String nome, String sobrenome, LocalDate dtNascimento) {
	        super();
	        this.id = id;
	        this.nome = nome;
	        this.sobrenome = sobrenome;
	        this.dtNascimento = dtNascimento;
	    }
	     
	    public Usuario(Long id, String nome, String sobrenome, LocalDate dtNascimento, TipoSexo sexo) {
	        super();
	        this.id = id;
	        this.nome = nome;
	        this.sobrenome = sobrenome;
	        this.dtNascimento = dtNascimento;
	        this.sexo = sexo;
	    }
	 
	    public Long getId() {
	        return id;
	    }
	 
	    public void setId(Long id) {
	        this.id = id;
	    }
	 
	    public String getNome() {
	        return nome;
	    }
	 
	    public void setNome(String nome) {
	        this.nome = nome;
	    }
	 
	    public String getSobrenome() {
	        return sobrenome;
	    }
	 
	    public void setSobrenome(String sobrenome) {
	        this.sobrenome = sobrenome;
	    }
	     
	    public LocalDate getDtNascimento() {
	        return dtNascimento;
	    }
	 
	    public void setDtNascimento(LocalDate dtNascimento) {
	        this.dtNascimento = dtNascimento;
	    }
	 
	    public TipoSexo getSexo() {
	        return sexo;
	    }
	 
	    public void setSexo(TipoSexo sexo) {
	        this.sexo = sexo;
	    }
	    
	    // é uma boa prática quando utiliza framework ORM gerar os metodos equals e hashCode 
	    @Override
	    public String toString() {
	        return "Usuario [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + "]";
	    }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Usuario other = (Usuario) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

}
