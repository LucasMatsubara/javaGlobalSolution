package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.UsuarioRequestDTO;
import br.com.fiap.aegis.dto.UsuarioResponseDTO;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.Usuario;
import br.com.fiap.aegis.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogOperacaoService logService;

    public Page<UsuarioResponseDTO> listarTodosPaginado(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(this::mapearParaResponseDTO);
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado com ID: " + id));
        return mapearParaResponseDTO(usuario);
    }

    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado com ID: " + id));

        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setRole(dto.role());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        logService.registarAcao(usuarioAtualizado.getEmail(), "Dados cadastrais atualizados pelo utilizador.", "SISTEMA");

        return mapearParaResponseDTO(usuarioAtualizado);
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado com ID: " + id));

        usuarioRepository.delete(usuario);
        logService.registarAcao(usuario.getEmail(), "Conta de utilizador encerrada e excluída do sistema.", "ALTO");
    }

    private UsuarioResponseDTO mapearParaResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getEmail(), usuario.getRole());
    }
}