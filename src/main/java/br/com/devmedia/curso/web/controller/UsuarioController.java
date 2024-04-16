package br.com.devmedia.curso.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.devmedia.curso.dao.UsuarioDao;
import br.com.devmedia.curso.domain.TipoSexo;
import br.com.devmedia.curso.domain.Usuario;

@Controller
// define um caminho (path), quando utiliza apenas o atributo value dentro da notação RequestMapping não precisa adicionar o atributo value
// basta adicionar a string por padrão vai entender que esse valor seria respectivo ao value
// para acessar esse controller depois da / principal da aplicação nome usuario ou string usuario 
@RequestMapping("usuario")
public class UsuarioController {
	
	// injetar dentro do controller 
	@Autowired
    private UsuarioDao dao;
	
	// devolve a lista de usuarios
    @RequestMapping(value = "/todos", method = RequestMethod.GET)
    public ModelAndView home(ModelMap model) {
    	// para adicionar o quer enviar para a página
    	// variavel usuarios  vai armazenar a lista 
        model.addAttribute("usuarios", dao.getTodos());
        // coloca no parâmetro  o destino que é a página que eu quero enviar
        // é necessário colocar o diretorio "user" pois a página encontra-se lá
        // depois coloca objeto que quer enviar
        
       // se a pagina de layout não for retornada como  resposta pelo controller não vai dar certo, pois antes ele retornava list.html e add.html
       // só que essas 2 páginas foi removido os conteudos referentes ao rodapé e cabeçalho
      //  return new ModelAndView("/user/list", model);
        
        // precisa dizer qual pagina vai ser aberta como um conteudo ao div referente ao conteúdo 
       // colocar na variavel conteudo o valor /user/list que á pagina list.html que vai abrir como resposta
        // lá no div como referente ao fragmento do corpo da página 
        model.addAttribute("conteudo", "/user/list");
        
        // agora tem um retorno para uma pagina de layout
        return new ModelAndView("layout", model);
    }
     
	// consulta nome
    @GetMapping("/nome")
    public ModelAndView listarPorNome(@RequestParam(value = "nome") String nome, ModelMap model) {
 
        if (nome == null) {
            return new ModelAndView("redirect:/usuario/todos");
        }
        model.addAttribute("usuarios", dao.getByNome(nome));
        model.addAttribute("conteudo", "/user/list");
        
        return new ModelAndView("layout", model);
    }   
     
 // consulta tipo sexo
    @GetMapping("/sexo")
    public ModelAndView listarPorSexo(@RequestParam(value = "tipoSexo") TipoSexo sexo, ModelMap model) {
    	// se for nulo ele retorna para todos
        if (sexo == null) {
            return new ModelAndView("redirect:/usuario/todos");
        }
        model.addAttribute("usuarios", dao.getBySexo(sexo));
        model.addAttribute("conteudo", "/user/list");
        return new ModelAndView("layout", model);
    }
    

    @GetMapping("/cadastro")
    // por que usar o ModelAttribute porque a página add.jsp tem um formulario e nesse formulário tem um model attribute "usuario"
    // o  tipo de objeto que vai trabalhar no formulario? Um objeto do tipo "usuario", define aqui objeto do tipo "usuario" 
    public ModelAndView cadastro(@ModelAttribute("usuario") Usuario usuario) {
    	//tira a linha seguinte pois criou um metodo de deixa a lista de sexo exposto
       // model.addAttribute("sexos", TipoSexo.values());
    	
    	   // se a pagina de layout não for retornada como  resposta pelo controller não vai dar certo, pois antes ele retornava list.html e add.html
        // só que essas 2 páginas foi removido os conteudos referentes ao rodapé e cabeçalho
       //  return new ModelAndView("/user/add", model);

         // precisa dizer qual pagina vai ser aberta como um conteudo ao div referente ao conteúdo 
        // colocar na variavel conteudo o valor /user/add que á pagina add.html que vai abrir como resposta
         // lá no div como referente ao fragmento do corpo da página, ao inves de fazer um model 
    	// chamar o metodo addAttribute colocando a variavel e o conteúdo como o código abaixo comentado
    	// pode fazer isso no ModelAndView direto
        // model.addAttribute("conteudo", "/user/add");
    	
    	// agora tem um retorno para uma pagina de layout
    	return new ModelAndView("layout", "conteudo", "/user/add");
    }
     
    // tem objeto usuario declarado no parametro,  esse objeto usuario é o objeto que vai receber os dados enviados pelo formulario
    @PostMapping("/save") // ou pode usar @RequestMapping(value = "/save", method = RequestMethod.POST) que é a mesma coisa
    // quando faz um redirect não pode enviar mensagem via ModelMap! Porque o ModelMapping funciona apenas no sistema forward 
    // que é quando você não faz um redirect, se faz o redirect usando o modelMap fazendo uma nova solicitação os dados do Model se perdem
    // usa o RedirectAttributes que tem o metodo addFlashAttribute que proporciona enviar para página no redirect qualquer tipo de variavél 
    // @Valid vai validar objeto usuário que está chegando no controller, de forma positiva ou negativa fo formulario enviado do controller
    public ModelAndView save(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {
      // além do da validação pode adicionar uma espécie de alteração de fluxo da solicação quando tem um problema de validação
     // exemplo o campo não venho preenchido quer retornar para o formulário com a mensagem da validação
    // BindingResult esse objeto deve esta obrigatoriamente antes do  RedirectAttributes caso coloque depois o Spring não vai reconhecer
    	
   // o fluxo dessa requisição vai cair aqui onde testa objeto result  se for verdadeiro significa ouve um problema de validação e 
    //	deve ser enviado novamente pro formulario e lá no fomulario exibir a mensagem que esta cadastrada 
   // 	nas anotações de validação na classe de usuario 
    	if (result.hasErrors()) {
    		return new ModelAndView("layout", "conteudo", "/user/add");
        }
    	 dao.salvar(usuario);
         attr.addFlashAttribute("message", "Registro inserido com sucesso.");
         return new ModelAndView("redirect:/usuario/todos");
    }
     
    // espera uma por uma requisição a partir do botão editar da pagina, essa requisição vai ter como parte da URL o ID do usuario que devera ser alterado, 
    // esse id será utilizado para fazer uma consulta no banco de dados e recuperar os dados desse  usuario então esses dados recuperados 
     //vão ser enviados como resposta para pagina do fomulario e os dados do usuario vão aparecer preenchidos em cada um dos campos tem no formulario 
    // ao enviar os dados para o formulario vai ser preciso recuperar e armazenar no formulario o valor referente ao id do usuario
    // por que é importante na operação de update quando trabalha com Hibernate que tenha o id do objeto está sendo alterado 
    
    // @PathVariable essa anotação captura o valor que a gente está recebendo na URL adiciona ela ao um objeto
    @GetMapping("/update/{id}")
    public ModelAndView preUpdate(@PathVariable("id") Long id, ModelMap model) {
    	  model.addAttribute("usuario", dao.getId(id));
          model.addAttribute("conteudo", "/user/add");
          return new ModelAndView("layout", model);
    }
     
    // usa @ModelAttribute por que está recebendo do fomulário um objeto usuario dessa vez
    // RedirectAttributes enviar a mensagem para front
    @PostMapping("/update")
    public ModelAndView update(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
        	return new ModelAndView("layout", "conteudo", "/user/add");
        }
        dao.editar(usuario);
        attr.addFlashAttribute("message", "Registro editado com sucesso.");
        return new ModelAndView("redirect:/usuario/todos");
    }
     
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes attr) {
        dao.excluir(id);
        attr.addFlashAttribute("message", "Usuário excluído com sucesso.");
        return "redirect:/usuario/todos";
    }
    
	// deixa lista de sexo disponivel @ModelAttribute somente chamando "sexo" no front 
	@ModelAttribute("sexos")
    public TipoSexo[] tipoSexo() {
        return TipoSexo.values();
    }
     

}
