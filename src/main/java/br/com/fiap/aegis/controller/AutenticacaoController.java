package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.AuthenticationDTO;
import br.com.fiap.aegis.dto.LoginResponseDTO;
import br.com.fiap.aegis.dto.RegisterDTO;
import br.com.fiap.aegis.dto.RegisterResponseDTO;
import br.com.fiap.aegis.model.Empresa;
import br.com.fiap.aegis.model.Usuario;
import br.com.fiap.aegis.repository.EmpresaRepository;
import br.com.fiap.aegis.repository.UsuarioRepository;
import br.com.fiap.aegis.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para Login e Registo de utilizadores da plataforma AEGIS")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Login de Utilizador", description = "Valida as credenciais e retorna o Token JWT com empresaId e nome da empresa")
    public ResponseEntity<EntityModel<LoginResponseDTO>> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        Usuario usuario = (Usuario) auth.getPrincipal();
        var token = tokenService.gerarToken(usuario);

        Long empresaId = usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null;
        String nomeEmpresa = usuario.getEmpresa() != null ? usuario.getEmpresa().getNome() : "Aegis Corp";

        LoginResponseDTO response = new LoginResponseDTO(token, empresaId, nomeEmpresa);

        EntityModel<LoginResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(AutenticacaoController.class).slash("login").withSelfRel());
        resource.add(linkTo(methodOn(DashboardController.class).obterResumo()).withRel("dashboard-resumo"));

        return ResponseEntity.ok(resource);
    }

    @PostMapping("/register")
    @Operation(summary = "Registar Utilizador", description = "Cria um novo utilizador vinculado a uma empresa existente")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterDTO data) {
        if (this.usuarioRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(data.email());
        novoUsuario.setSenha(encryptedPassword);
        novoUsuario.setRole(data.role());

        // Vincula empresa se o empresaId foi fornecido
        if (data.empresaId() != null) {
            empresaRepository.findById(data.empresaId()).ifPresent(novoUsuario::setEmpresa);
        }

        this.usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok(new RegisterResponseDTO("Usuário registrado com sucesso!", "/api/auth/login"));
    }
}