package br.com.fiap.aegis.security;

import br.com.fiap.aegis.model.Usuario;
import br.com.fiap.aegis.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class EmpresaResolver {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Long getEmpresaIdDoUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;

        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findUsuarioByEmail(email);
        if (usuario == null || usuario.getEmpresa() == null) return null;

        return usuario.getEmpresa().getId();
    }
}