package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.AuthenticationDTO;
import br.com.fiap.aegis.dto.LoginResponseDTO;
import br.com.fiap.aegis.dto.RegisterDTO;
import br.com.fiap.aegis.dto.RegisterResponseDTO;
import br.com.fiap.aegis.model.Usuario;
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
    private TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Login de Utilizador", description = "Valida as credenciais e retorna o Token JWT acompanhado de caminhos para a Dashboard")
    public ResponseEntity<EntityModel<LoginResponseDTO>> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
        LoginResponseDTO response = new LoginResponseDTO(token);

        EntityModel<LoginResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(AutenticacaoController.class).slash("login").withSelfRel());
        resource.add(linkTo(methodOn(DashboardController.class).obterResumo()).withRel("dashboard-resumo"));

        return ResponseEntity.ok(resource);
    }

    @PostMapping("/register")
    @Operation(summary = "Registar Utilizador", description = "Cria um novo utilizador com password encriptada e retorna link para direcionar ao login")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterDTO data) {
        if (this.usuarioRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        Usuario novoUsuario = new Usuario(null, data.email(), encryptedPassword, data.role());
        this.usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok(new RegisterResponseDTO("Usuário registrado com sucesso!", "/api/auth/login"));
    }
}